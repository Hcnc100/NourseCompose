package com.nullpointer.noursecompose.services.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nullpointer.noursecompose.core.utils.correctFlag
import com.nullpointer.noursecompose.services.alarms.AlarmReceiver.Companion.ID_ALARM
import com.nullpointer.noursecompose.services.alarms.AlarmReceiver.Companion.WAKE_UP_ALARM


object MyAlarmManager {

    private fun getPendingAlarm(
        context: Context,
        idAlarm: Long,
        myAction: String = WAKE_UP_ALARM,
    ): PendingIntent? {
        val randomRequestCode = "$idAlarm${(1..999).random()}".toInt()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = myAction
            putExtra(ID_ALARM, idAlarm)
        }
        return PendingIntent.getBroadcast(
            context,
            randomRequestCode, intent,
            context.correctFlag
        )
    }

    fun setAlarm(context: Context, idAlarm: Long, nextAlarm: Long) {
        startAlarmExactly(context, idAlarm, nextAlarm)
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
}