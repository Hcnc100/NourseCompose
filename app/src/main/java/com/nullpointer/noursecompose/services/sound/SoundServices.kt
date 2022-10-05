package com.nullpointer.noursecompose.services.sound

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.nullpointer.noursecompose.domain.sound.SoundRepository
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.notify.TypeNotify
import com.nullpointer.noursecompose.models.notify.TypeNotify.ALARM
import com.nullpointer.noursecompose.models.notify.TypeNotify.NOTIFY
import com.nullpointer.noursecompose.services.NotificationHelper
import com.nullpointer.noursecompose.services.sound.SoundServicesControl.KEY_ALARM_PASS
import com.nullpointer.noursecompose.services.sound.SoundServicesControl.KEY_START_SOUND
import com.nullpointer.noursecompose.services.sound.SoundServicesControl.KEY_STOP_SOUND
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SoundServices : LifecycleService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var soundRepository: SoundRepository

    lateinit var alarmPassed: Alarm

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
                            val typeAlarm = soundRepository.typeNotify.first()
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
        soundRepository.startSoundInLoopAlarm()
        timer.start()
        // * vibrate phone
        while (soundRepository.isPlaying.first()) {
            vibratePhone()
            delay(1000)
        }
    }

    private fun cancelAlarm() = lifecycleScope.launch {
        soundRepository.stopSoundInLoopAlarm()
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