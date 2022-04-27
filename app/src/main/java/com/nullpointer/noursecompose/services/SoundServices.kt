package com.nullpointer.noursecompose.services

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.notify.TypeNotify.*
import com.nullpointer.noursecompose.ui.screen.config.getUriMediaPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var prefRepoImpl: PrefRepoImpl

    lateinit var alarmPassed: Alarm

    private val mediaPlayer = MediaPlayer()

    private var isSound = false

    private val listAlarmWait = mutableListOf<Alarm>()
    private var jobAwaitAlarm: Job? = null


    private val timer = object : CountDownTimer(MINUTE_IN_MILLIS, 1000) {
        override fun onTick(millisUntilFinished: Long) = Unit
        override fun onFinish() {
            notificationHelper.showNotificationLost(alarmPassed)
            cancelAlarm()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                KEY_START_SOUND -> {
                    it.getParcelableExtra<Alarm>(KEY_ALARM_PASS)?.let { alarm ->
                        lifecycleScope.launch {
                            launchAlarms(alarm)
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

    private suspend fun launchAlarms(alarm: Alarm) {
        val typeAlarm = prefRepoImpl.typeNotify.first()
        synchronized(this) {
            when (typeAlarm) {
                NOTIFY -> {
                    listAlarmWait.add(alarm)
                    jobAwaitAlarm?.cancel()
                    jobAwaitAlarm = lifecycleScope.launch {
                        delay(1000)
                        notificationHelper.showNotifyAlarm(listAlarmWait)
                        listAlarmWait.clear()
                    }
                }
                ALARM -> {
                    listAlarmWait.add(alarm)
                    jobAwaitAlarm?.cancel()
                    jobAwaitAlarm = lifecycleScope.launch {
                        delay(1000)
                        val first = listAlarmWait.removeFirst()
                        if (listAlarmWait.isNotEmpty()) notificationHelper.showNotificationLost(listAlarmWait)
                        startForeground(1, notificationHelper.getNotifyAlarmFromServices(first))
                        launchAlarm(first)
                    }
                }
            }
        }

    }

    private fun launchAlarm(alarm: Alarm) {
        alarmPassed = alarm
        alarmIsAlive.value = true
        isSound = true
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
        alarmIsAlive.value = null
    }

}