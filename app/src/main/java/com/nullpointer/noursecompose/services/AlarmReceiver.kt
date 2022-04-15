package com.nullpointer.noursecompose.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.widget.Toast
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.getTimeNow
import com.nullpointer.noursecompose.core.utils.myGoAsync
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.models.registry.Registry
import com.nullpointer.noursecompose.models.registry.TypeRegistry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


@OptIn(DelicateCoroutinesApi::class)
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
        const val TIME_SET = "android.intent.action.TIME_SET"
        const val WAKE_UP_ALARM = "WAKE_UP_ALARM"
        const val ID_ALARM = "ID_ALARM"
    }

    @Inject
    lateinit var alarmRepository: AlarmRepoImpl

//    @Inject
//    lateinit var registryRepository: RegistryRepoImpl

    override fun onReceive(context: Context, intent: Intent) {
        intent.let {
            when (it.action) {
                BOOT_COMPLETED, TIME_SET -> restoreAlarms(context)
                WAKE_UP_ALARM -> launchAlarm(context, it.getLongExtra(ID_ALARM, -1))
                else -> Timber.d("action intent $it")
            }
        }
    }

    private fun launchAlarm(
        context: Context,
        idAlarm: Long,
    ) = myGoAsync(GlobalScope, Dispatchers.Default) {
        val alarm = alarmRepository.getAlarmById(idAlarm)
        val currentTime = getTimeNow()
        if (alarm != null) {
            // * launch notification from alarm
            NotificationManagerHelper(context).showNotificationAlarm(
                alarm.title,
                alarm.nameFile?.let { ImageUtils.loadImageFromStorage(it, context) },
                alarm.message
            )
            // * registry launch
            registryRepository.addNewRegistry(Registry(idAlarm = idAlarm, type = TypeRegistry.LAUNCH))
            updateAlarm(alarm, currentTime, context)
        } else {
            // * registry error
            registryRepository.addNewRegistry(Registry(idAlarm = idAlarm, type = ERROR_LAUNCH))
            Timber.e("Alarm id non found $idAlarm")
        }
    }

    private suspend fun updateAlarm(
        alarm: Alarm,
        currentTime: Long,
        context: Context,
        typeRegistry: TypeRegistry? = null,
    ) {
        // ? update alarm
        val alarmUpdate = when (alarm.typeAlarm) {
            // * if it's one shot only launch once and desactivate
            AlarmTypes.ONE_SHOT -> alarm.copy(isActive = false, nextAlarm = null)
            // * if it's indefinite only update new next alarm
            AlarmTypes.INDEFINITELY -> alarm.updateTime(currentTime)
            // ! if the alarm is in range, else desactivate
            AlarmTypes.RANGE -> {
                val rangeAlarm = (alarm.rangeInitAlarm!!..alarm.rangeFinishAlarm!!)
                if (currentTime in rangeAlarm)
                    alarm.updateTime(currentTime)
                else alarm.copy(isActive = false, nextAlarm = null)
            }
        }

        // * remove alarms it's needing
        alarmRepository.updateAlarm(alarmUpdate)

        // * if the option is restore, so
        if (alarmUpdate.isActive) {
            MyAlarmManager.setAlarm(context, alarmUpdate) {
                typeRegistry?.let {
                    registryRepository.addNewRegistry(Registry(type = it, idAlarm = alarmUpdate.id!!))
                }
            }
        } else {
            MyAlarmManager.cancelAlarm(context, alarmUpdate.id!!) {
                registryRepository.addNewRegistry(Registry(type = UNREGISTRY,
                    idAlarm = alarmUpdate.id))
            }
        }
    }


    private fun restoreAlarms(context: Context) = myGoAsync(GlobalScope, Dispatchers.Default) {
        // * system to restore alarms active
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Se inicio el proceso de restauracion", Toast.LENGTH_LONG)
                .show()
        }

        Timber.d("Se inicio el proceso de restauracion de las alarmas")
        val alarmsActive = alarmRepository.getAllAlarmActive()
        val currentTime = getTimeNow() + MINUTE_IN_MILLIS
        alarmsActive.forEach { alarm ->
            if (currentTime > alarm.nextAlarm ?: 0) {
                // * notify in group that alarm is lost
                NotificationManagerHelper(context).showNotificationLost(
                    alarm.title,
                    alarm.nameFile?.let { ImageUtils.loadImageFromStorage(it, context) },
                    alarm.message,
                    alarm.nextAlarm ?: currentTime
                )
                updateAlarm(alarm, currentTime, context, TypeRegistry.RESTORE)
            } else {
                MyAlarmManager.setAlarm(context, alarm) {
                    registryRepository.addNewRegistry(Registry(type = TypeRegistry.RESTORE,
                        idAlarm = alarm.id!!))
                }
            }

        }
        Timber.d("Alarmas restauradas ${alarmsActive.size}")
    }


}