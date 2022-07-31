package com.nullpointer.noursecompose.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.getTimeNow
import com.nullpointer.noursecompose.core.utils.myGoAsync
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.models.registry.Log
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
        const val KEY_RESTORE = "com.nullpointer.noursecompose.android.action.broadcast"
        const val WAKE_UP_ALARM = "WAKE_UP_ALARM"
        const val ID_ALARM = "ID_ALARM"
    }

    @Inject
    lateinit var alarmRepository: AlarmRepoImpl

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var prefRepoImpl: PrefRepoImpl


    override fun onReceive(context: Context, intent: Intent) {
        intent.let {
            when (it.action) {
                KEY_RESTORE, BOOT_COMPLETED, TIME_SET -> restoreAlarms(context,
                    it.action == KEY_RESTORE)
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

            SoundServices.startServices(context, alarm)

            // * registry launch
            alarmRepository.addNewLog(Log(idAlarm = idAlarm, type = TypeRegistry.LAUNCH))
            updateAlarm(alarm, currentTime)
        } else {
            // * registry error
            alarmRepository.addNewLog(Log(idAlarm = idAlarm,
                type = TypeRegistry.ERROR_LAUNCH))
            Timber.e("Alarm id non found $idAlarm")
        }
    }

    private suspend fun updateAlarm(
        alarm: Alarm,
        currentTime: Long
    ) {
        // ? update alarm
        val alarmUpdate = when (alarm.typeAlarm) {
            // * if it's one shot only launch once and deactivate
            AlarmTypes.ONE_SHOT -> alarm.copy(isActive = false, nextAlarm = null)
            // * if it's indefinite only update new next alarm
            AlarmTypes.INDEFINITELY -> alarm.updateTime(currentTime)
            // ! if the alarm is in range, else deactivate
            AlarmTypes.RANGE -> {
                val rangeAlarm = (alarm.rangeInitAlarm!!..alarm.rangeFinishAlarm!!)
                if (currentTime in rangeAlarm)
                    alarm.updateTime(currentTime) else alarm.copy(isActive = false,
                    nextAlarm = null)
            }
        }

        // * remove alarms it's needing
        alarmRepository.updateAlarm(alarmUpdate)
    }


    private fun restoreAlarms(context: Context, isRestoreUser: Boolean) =
        myGoAsync(GlobalScope, Dispatchers.IO) {
            val listAlarmLost = mutableListOf<Alarm>()
            Timber.d("Se inició el proceso de restauración de las alarmas")
            withContext(Dispatchers.Main){
                if (isRestoreUser) Toast.makeText(context,
                    context.getString(R.string.init_process_restart),
                    Toast.LENGTH_SHORT).show()
            }
            val alarmsActive = alarmRepository.getAllAlarmActive()
            val currentTime = getTimeNow()
            alarmsActive.forEach { alarm ->
                if (currentTime > (alarm.nextAlarm ?: 0)) {
                    // * notify in group that alarm is lost
                    listAlarmLost.add(alarm)
                    updateAlarm(alarm, currentTime)
                } else {
                    alarmRepository.restoreAlarm(alarm)
                }
            }
            if (listAlarmLost.isNotEmpty()) {
                notificationHelper.showNotificationLost(listAlarmLost)
                listAlarmLost.clear()
            }

            Timber.d("Alarmas restauradas ${alarmsActive.size}")
        }
}