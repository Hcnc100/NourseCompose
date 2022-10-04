package com.nullpointer.noursecompose.core.delegates

import android.text.format.DateUtils
import androidx.lifecycle.SavedStateHandle
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.*
import com.nullpointer.noursecompose.models.alarm.AlarmTypes

class PropertySavableAlarmTime(
    state: SavedStateHandle
) {
    companion object {
        private const val KEY_RANGE = "KEY_RANGE"
        private const val KEY_TIME_INIT = "KEY_TIME_INIT"
        private const val KEY_TYPE_ALARM = "KEY_TYPE_ALARM"
        private const val KEY_ERROR_RANGE = "KEY_ERROR_RANGE"
        private const val KEY_NEXT_ALARM = "KEY_NEXT_ALARM"
        private const val KEY_TIME_REPEAT = "KEY_TIME_REPEAT"
        const val DEFAULT_RESOURCE = -1
    }


    var typeAlarm: AlarmTypes by SavableComposeState(state, KEY_TYPE_ALARM, AlarmTypes.ONE_SHOT)
        private set

    var timeInitAlarm: Long by SavableComposeState(state, KEY_TIME_INIT, getTimeNowToClock())
        private set

    var rangeAlarm by SavableComposeState(state, KEY_RANGE, Pair(0L, 0L))
        private set

    var timeNextAlarm by SavableComposeState(state, KEY_NEXT_ALARM, timeInitAlarm)
        private set

    var timeToRepeatAlarm by SavableComposeState(
        state,
        KEY_TIME_REPEAT,
        8 * DateUtils.HOUR_IN_MILLIS
    )
        private set

    var errorRange by SavableComposeState(state, KEY_ERROR_RANGE, DEFAULT_RESOURCE)
    val hasErrorRange get() = errorRange != DEFAULT_RESOURCE

    init {
        calculateNextAlarm()
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
        val currentTime = getTimeNow() + DateUtils.MINUTE_IN_MILLIS
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

    fun clearValue() {
        changeType(AlarmTypes.ONE_SHOT)
    }

}