package com.nullpointer.noursecompose.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.getTimeNow
import com.nullpointer.noursecompose.core.utils.myGoAsync
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.models.registry.Registry
import com.nullpointer.noursecompose.models.registry.TypeRegistry
import com.nullpointer.noursecompose.ui.activitys.MainActivity
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

    private lateinit var notificationHelper: NotificationHelper



    override fun onReceive(context: Context, intent: Intent) {
        notificationHelper = NotificationHelper(context)
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
                SoundServices.startServices(context,alarm)
            // * registry launch
            alarmRepository.addNewRegistry(Registry(idAlarm = idAlarm, type = TypeRegistry.LAUNCH))
            updateAlarm(alarm, currentTime, context)
        } else {
            // * registry error
            alarmRepository.addNewRegistry(Registry(idAlarm = idAlarm, type = TypeRegistry.ERROR_LAUNCH))
            Timber.e("Alarm id non found $idAlarm")
        }
    }

    private suspend fun updateAlarm(
        alarm: Alarm,
        currentTime: Long,
        context: Context,
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
        alarmRepository.updateAlarm(alarmUpdate,context)
    }


    private fun restoreAlarms(context: Context) = myGoAsync(GlobalScope, Dispatchers.Default) {
        // * system to restore alarms active
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Se inicio el proceso de restauracion", Toast.LENGTH_LONG)
                .show()
        }

        Timber.d("Se inicio el proceso de restauracion de las alarmas")
        val alarmsActive = alarmRepository.getAllAlarmActive()
        val currentTime = getTimeNow()
        alarmsActive.forEach { alarm ->
            if (currentTime > alarm.nextAlarm ?: 0) {
                // * notify in group that alarm is lost
                notificationHelper.showNotificationLost(
                    alarm.title,
                    alarm.nameFile?.let { ImageUtils.loadImageFromStorage(it, context) },
                    alarm.message,
                    alarm.nextAlarm ?: currentTime
                )
                updateAlarm(alarm, currentTime, context)
            } else {
                alarmRepository.restoreAlarm(alarm, context)
            }
        }
        Timber.d("Alarmas restauradas ${alarmsActive.size}")
    }
}