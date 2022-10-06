package com.nullpointer.noursecompose.ui.activitys

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nullpointer.noursecompose.core.utils.turnScreenOffAndKeyguardOn
import com.nullpointer.noursecompose.core.utils.turnScreenOnAndKeyguardOff
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.presentation.AlarmSoundViewModel
import com.nullpointer.noursecompose.services.sound.SoundServicesControl.KEY_ALARM_PASS_ACTIVITY
import com.nullpointer.noursecompose.ui.screen.sound.AlarmSoundScreen
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmScreen : AppCompatActivity() {

    private val alarmSoundViewModel: AlarmSoundViewModel by viewModels()
    private val context get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        turnScreenOnAndKeyguardOff()
        val alarm = intent.extras?.getParcelable<Alarm>(KEY_ALARM_PASS_ACTIVITY)!!
        setContent {
            NourseComposeTheme {
                val alarmIsSound by alarmSoundViewModel.isAlarmSound.collectAsState()
                LaunchedEffect(key1 = alarmIsSound) { if (!alarmIsSound) context.finish() }
                AlarmSoundScreen(alarmSound = alarm)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}
