package com.nullpointer.noursecompose.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.ui.activitys.AlarmScreen
import com.nullpointer.noursecompose.ui.screen.config.getUriMediaPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SoundServices : LifecycleService() {
    companion object {

        val alarmIsAlive = mutableStateOf<Boolean?>(null)

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

    @Inject
    lateinit var prefRepoImpl: PrefRepoImpl

    lateinit var alarmPassed: Alarm

    private val mediaPlayer = MediaPlayer()

    private var isSound = false


    private val timer = object : CountDownTimer(MINUTE_IN_MILLIS, 1000) {
        override fun onTick(millisUntilFinished: Long) = Unit
        override fun onFinish() {
            notificationHelper.showNotificationLost(alarmPassed)
            cancelAlarm()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationHelper = NotificationHelper(this)
        intent?.let {
            when (it.action) {
                KEY_START_SOUND -> {
                    it.getParcelableExtra<Alarm>(KEY_ALARM_PASS)?.let { alarm ->
                            synchronized(this){
                                if(isSound){
                                    notificationHelper.showNotificationLost(alarm)
                                    Timber.d("Alarma perdida $alarm")
                                }else{
                                    isSound=true
                                    alarmPassed = alarm
                                    startForeground(1, notificationHelper.getNotificationAlarm(alarm))
                                    launchAlarm()
                                    Timber.d("Alarma sonando $alarm")
                                }
                            }
                    }
                }
                KEY_STOP_SOUND -> {
                    timer.cancel()
                    cancelAlarm()
                }
                else -> Timber.d("action ${it.action}")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun launchAlarm() {
        alarmIsAlive.value = true
        lifecycleScope.launch {
            val index = prefRepoImpl.intSound.first()
            val sound = getUriMediaPlayer(index, this@SoundServices)
            mediaPlayer.setDataSource(this@SoundServices, sound)
            mediaPlayer.prepare()
            mediaPlayer.start()
            timer.start()
            mediaPlayer.setOnCompletionListener {
                if (isSound) {
                    mediaPlayer.seekTo(0)
                    mediaPlayer.start()
                }
            }
            while (isSound) {
                vibratePhone()
                delay(1000)
            }
        }
    }

    private fun cancelAlarm() {
        isSound = false
        alarmIsAlive.value = false
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.release()
        stopForeground(true)
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmIsAlive.value=null
    }

}