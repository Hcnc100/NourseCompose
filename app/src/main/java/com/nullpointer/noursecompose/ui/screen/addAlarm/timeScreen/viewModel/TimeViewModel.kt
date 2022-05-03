package com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.viewModel

import android.text.format.DateUtils
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.core.utils.*
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
    state: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val KEY_TIME_INIT = "KEY_TIME_INIT"
        private const val KEY_ERROR_RANGE = "KEY_ERROR_RANGE"
        private const val KEY_TYPE_ALARM = "KEY_TYPE_ALARM"
        private const val KEY_RANGE = "KEY_RANGE"
        private const val KEY_NEXT_ALARM = "KEY_NEXT_ALARM"
        private const val KEY_TIME_REPEAT = "KEY_TIME_REPEAT"
        private const val KEY_VM_TIME_INIT = "KEY_VM_TIME_INIT"
    }

    var typeAlarm: AlarmTypes by SavableComposeState(state, KEY_TYPE_ALARM, AlarmTypes.INDEFINITELY)
        private set

    var timeInitAlarm: Long by SavableComposeState(state, KEY_TIME_INIT, getTimeNowToClock())
        private set

    var rangeAlarm: Pair<Long, Long> by SavableComposeState(state, KEY_RANGE, Pair(0L, 0L))
        private set

    var timeNextAlarm by SavableComposeState(state, KEY_NEXT_ALARM, timeInitAlarm)
        private set

    var timeToRepeatAlarm by SavableComposeState(state,
        KEY_TIME_REPEAT, 8 * DateUtils.HOUR_IN_MILLIS)
        private set

    var errorRange: Int by SavableComposeState(state, KEY_ERROR_RANGE, -1)
    val hasErrorRange get() = errorRange != -1

    private var isInit: Boolean by SavableComposeState(state, KEY_VM_TIME_INIT, false)

    val timeRepeatIsValid get() = timeToRepeatAlarm!=0L

    init {
        calculateNextAlarm()
    }

    fun changeInit(alarm: Alarm) {
        if (!isInit) {
            changeType(alarm.typeAlarm)
            alarm.repeaterEvery?.let { timeToRepeatAlarm = it }
            alarm.rangeInitAlarm?.let { init ->
                alarm.rangeFinishAlarm?.let { fin ->
                    if (init <= getFirstTimeDay()) rangeAlarm = Pair(init, fin)
                }
            }
            alarm.nextAlarm?.let {
                if (it > getTimeNow() + 10 * MINUTE_IN_MILLIS) {
                    timeNextAlarm = it
                    timeInitAlarm = it
                }
            }
            isInit = false
        }
    }

    fun changeTimeToRepeatAlarm(newHour: Long) {
        timeToRepeatAlarm = newHour
        if (newHour > 0)
            calculateNextAlarm()
    }

    fun changeTimeInitAlarm(newTime: Long) {
        timeInitAlarm = newTime
        calculateNextAlarm()
    }

    fun changeType(newType: AlarmTypes) {
        typeAlarm = newType
        timeToRepeatAlarm = 8 * DateUtils.HOUR_IN_MILLIS
        if (newType == AlarmTypes.RANGE) {
            val firstTime = getFirstTimeDay()
            rangeAlarm = Pair(firstTime, firstTime + DateUtils.WEEK_IN_MILLIS)
        }
        calculateNextAlarm()
    }

    fun changeRangeAlarm(newRange: Pair<Long, Long>) {
        rangeAlarm = newRange
        // ! change text next alarm
        errorRange = when {
            newRange.second - newRange.first < DateUtils.DAY_IN_MILLIS -> R.string.error_time_min_range
            else -> -1
        }
        calculateNextAlarm()
    }


    private fun calculateNextAlarm() {
        // * get time now and add one minute of tolerance
        val currentTime = getTimeNow() + MINUTE_IN_MILLIS
        // * get time alarm
        val timeAlarmToday = timeInitAlarm

        timeNextAlarm = when (typeAlarm) {
            AlarmTypes.ONE_SHOT -> {
                // * if the alarm time in less than time now so next alarm is time indicate
                // * else if the time alarm is gather than current time, so the next alarm is tomorrow
                if (currentTime <= timeAlarmToday) timeAlarmToday else timeInitAlarm + DateUtils.DAY_IN_MILLIS
            }
            // * if the alarm time in less than time now so next alarm is time indicate
            // * else if the time alarm is gather than current time,so adding time repeater, until the next
            // * alarm time will more than the current time
            AlarmTypes.INDEFINITELY -> {
                if (currentTime <= timeAlarmToday) {
                    timeAlarmToday
                } else {
                    var nextAlarmAprox = timeAlarmToday
                    while (nextAlarmAprox <= currentTime) {
                        nextAlarmAprox += timeToRepeatAlarm
                    }
                    nextAlarmAprox
                }
            }
            // * if the alarm time in less than time now so next alarm is time indicate
            // * else if the time alarm is gather than current time, get the hour and minutes of the
            // * alarm, and get the fist time range and set the hour and minutes
            // * after, adding time to repeat so the time is more than the current time,
            // * this for the range to select, will select today
            AlarmTypes.RANGE -> {
                val (hour, minute) = getHourAndMinutesToday(timeInitAlarm)
                var nextAlarmAprox = setHourAndMinutesToday(hour, minute, rangeAlarm.first)
                while (nextAlarmAprox < currentTime) {
                    nextAlarmAprox += timeToRepeatAlarm
                }
                nextAlarmAprox
            }
        }
    }

}