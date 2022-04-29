package com.nullpointer.noursecompose.services

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.notify.TypeNotify
import com.nullpointer.noursecompose.models.notify.TypeNotify.ALARM
import com.nullpointer.noursecompose.models.notify.TypeNotify.NOTIFY
import com.nullpointer.noursecompose.ui.screen.config.getUriMediaPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SoundServices : LifecycleService() {
    companion object {

        var alarmIsAlive by mutableStateOf(false)

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

    private val mediaPlayer = MediaPlayer().apply {
        // * set alarm attributes
        setAudioAttributes(AudioAttributes.Builder()
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setLegacyStreamType(AudioManager.STREAM_ALARM)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build())
    }

    private var isSound = false

    private val listAlarmWait = mutableListOf<Alarm>()

    private var jobAwaitAlarm: Job? = null

    private val timer = object : CountDownTimer(MINUTE_IN_MILLIS, 1000) {
        override fun onTick(millisUntilFinished: Long) = Unit
        override fun onFinish() {
            // * if over time so, show notify lost alarm and cancel services
            notificationHelper.showNotificationLost(listOf(alarmPassed))
            cancelAlarm()
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                KEY_START_SOUND -> {
                    it.getParcelableExtra<Alarm>(KEY_ALARM_PASS)?.let { alarm ->
                        lifecycleScope.launch {
                            val typeAlarm = prefRepoImpl.typeNotify.first()
                            launchAlarms(alarm, typeAlarm)
                        }
                    }
                }
                KEY_STOP_SOUND -> {
                    timer.cancel()
                    cancelAlarm()
                }
                else -> Timber.e("action no valida ${it.action}")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @Synchronized
    private fun launchAlarms(alarm: Alarm, typeAlarm: TypeNotify) {
        listAlarmWait.add(alarm)
        jobAwaitAlarm?.cancel()
        jobAwaitAlarm = lifecycleScope.launch {
            delay(1000)
            when (typeAlarm) {
                NOTIFY -> {
                    // * get all alarm and notify
                    notificationHelper.showNotifyAlarm(listAlarmWait)
                    listAlarmWait.clear()
                }
                ALARM -> {
                    // * remove first alarm
                    val first = listAlarmWait.removeFirst()
                    // * if the list is not empty so, launch notify lost alarms
                    // ? this for no is possible sound more one alarm
                    if (listAlarmWait.isNotEmpty()) notificationHelper.showNotificationLost(
                        listAlarmWait)
                    // * init services for sound
                    startForeground(1, notificationHelper.getNotifyAlarmFromServices(first))
                    launchAlarm(first)
                }
            }
        }
    }

    private suspend fun launchAlarm(alarm: Alarm) {
        // * saved alarm passed
        // ? this if no stop alarm , and notify last alarm
        alarmPassed = alarm
        // * change state services to live
        // ? this for can stop activity observed this
        alarmIsAlive = true
        // * change var to control bucle alive
        isSound = true
        // * get type sound for alarm
        val index = prefRepoImpl.intSound.first()
        val sound = getUriMediaPlayer(index, this@SoundServices)
        // * prepare media player
        withContext(Dispatchers.IO) {
            mediaPlayer.setDataSource(this@SoundServices, sound)
            mediaPlayer.prepare()
        }
        // * start sound and count down
        // ? if the user no stop alarm in one minute, so  stop the services with help
        // ? of timer
        mediaPlayer.start()
        timer.start()
        // * loop media player
        // ? when stop , repeat
        mediaPlayer.setOnCompletionListener {
            if (isSound) {
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }
        // * vibrate phone
        while (isSound) {
            vibratePhone()
            delay(1000)
        }
    }

    private fun cancelAlarm() {
        // * break loop
        isSound = false
        // * notify dead services
        alarmIsAlive = false
        // * if media player is song to stop and release
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.release()
        // * stop services
        stopForeground(true)
        stopSelf()
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