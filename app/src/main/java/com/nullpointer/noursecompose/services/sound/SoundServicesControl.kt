package com.nullpointer.noursecompose.services.sound

import android.content.Context
import android.content.Intent
import com.nullpointer.noursecompose.models.alarm.Alarm

object SoundServicesControl {
    const val KEY_START_SOUND = "KEY_START_SOUND"
    const val KEY_STOP_SOUND = "KEY_STOP_SOUND"
    const val KEY_ALARM_PASS = "KEY_ALARM_PASS"
    const val KEY_ALARM_PASS_ACTIVITY = "KEY_ALARM_PASS_ACTIVITY"

    fun startServices(context: Context, alarm: Alarm) {
        Intent(context, SoundServices::class.java).also { intent ->
            intent.action = KEY_START_SOUND
            intent.putExtra(KEY_ALARM_PASS, alarm)
            context.startService(intent)
        }
    }

    fun stopServices(context: Context) {
        Intent(context, SoundServices::class.java).also { intent ->
            intent.action = KEY_STOP_SOUND
            context.startService(intent)
        }
    }
}