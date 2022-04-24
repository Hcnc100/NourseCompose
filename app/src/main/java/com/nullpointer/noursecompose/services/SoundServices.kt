package com.nullpointer.noursecompose.services

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class SoundServices : LifecycleService() {
    companion object {
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

    private lateinit var notificationHelper: NotificationHelper

    private val mediaPlayer by lazy {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        MediaPlayer.create(applicationContext, alarmSound)
    }
    private var isSound = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationHelper = NotificationHelper(this)
        intent?.let {
            when (it.action) {

                KEY_START_SOUND -> {
                    it.getParcelableExtra<Alarm>(KEY_ALARM_PASS)?.let { alarm ->
                        startForeground(1,  notificationHelper.getNotificationAlarm(alarm))
                        isSound = true
                        lifecycleScope.launch {
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener {
                                if (isSound) {
                                    mediaPlayer.seekTo(0)
                                    mediaPlayer.start()
                                }
                            }
                            while (isSound){
                                vibratePhone()
                                delay(1000)
                            }
                        }
                    }
                }
                KEY_STOP_SOUND -> {
                    isSound = false
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    stopForeground(true)
                }
                else -> Timber.d("action ${it.action}")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}