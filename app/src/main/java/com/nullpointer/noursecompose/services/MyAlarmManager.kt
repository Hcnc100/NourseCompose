package com.nullpointer.noursecompose.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.AlarmReceiver.Companion.ID_ALARM
import com.nullpointer.noursecompose.services.AlarmReceiver.Companion.WAKE_UP_ALARM
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
object MyAlarmManager {
    private const val REQUEST_CODE_ALARM = 1998

    private fun getPendingAlarm(
        context: Context,
        idAlarm: Long,
        myAction: String = WAKE_UP_ALARM,
    ): PendingIntent? {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = myAction
            putExtra(ID_ALARM, idAlarm)
        }
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    suspend fun setAlarm(context: Context, alarm: Alarm, actionBefore: suspend () -> Unit) {
        startAlarmExactly(context, alarm.id, alarm.nextAlarm!!)
        actionBefore()
    }

     fun cancelAlarm(context: Context, idAlarm: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingAlarm(context, idAlarm))
    }

    // ? this method registry alarm that launch exactly in the tome selected
    private fun startAlarmExactly(context: Context, idAlarm: Long, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = getPendingAlarm(context, idAlarm)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(timeInMillis, pending), pending)
    }


    fun cancelAlarms(context: Context, listId: List<Long>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        listId.forEach {
            alarmManager.cancel(getPendingAlarm(context, it))
        }
    }
}