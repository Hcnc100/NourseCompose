package com.nullpointer.noursecompose.core.utils

import android.content.Context
import android.text.format.DateUtils.*
import com.nullpointer.noursecompose.R
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    enum class TimeLaunch {
        TODAY, TOMORROW, OTHERS
    }

    fun timeRepeatInMillisToString(
        timeInMillis: Long,
        context: Context,
        includeSeconds: Boolean,
    ): String {
        var millis = timeInMillis
        require(millis >= 0) { "Duration must be greater than zero!" }
        val days: Long = TimeUnit.MILLISECONDS.toDays(millis)
        millis -= TimeUnit.DAYS.toMillis(days)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis)
        val sb = StringBuilder()
        if (days > 0)
            sb.append(" ${context.getTimeString(R.plurals.days, days)} ")
        if (hours > 0)
            sb.append(" ${context.getTimeString(R.plurals.hours, hours)} ")
        if (minutes > 0)
            sb.append(" ${context.getTimeString(R.plurals.minutes, minutes)} ")
        if (includeSeconds && minutes > 0)
            sb.append(" ${context.getTimeString(R.plurals.seconds, seconds)} ")

        return if (sb.isNotEmpty()) sb.toString() else context.getString(R.string.error_value_repeat_invalid)
    }

    fun getHourAndMinutesFromMillis(millis: Long): Pair<Int, Int> {
        val hourFromMillis = TimeUnit.MILLISECONDS.toHours(millis)
        return Pair(
            hourFromMillis.toInt(),
            (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hourFromMillis)).toInt()
        )
    }


    fun getTypeLaunchAlarm(timeInMillis: Long): TimeLaunch {
        val initTimeToday = getFirstTimeDay()
        val initTimeTomorrow = initTimeToday + DAY_IN_MILLIS
        return when (timeInMillis) {
            in initTimeToday until initTimeTomorrow -> TimeLaunch.TODAY
            in initTimeTomorrow until initTimeTomorrow + DAY_IN_MILLIS -> TimeLaunch.TOMORROW
            else -> TimeLaunch.OTHERS
        }
    }

    fun getStringTimeAboutNow(timeInMillis: Long, context: Context): String {
        val initTimeToday = getFirstTimeDay()
        val initTimeTomorrow = initTimeToday + DAY_IN_MILLIS
        return when (timeInMillis) {
            in initTimeToday until initTimeTomorrow ->
                context.getString(R.string.text_alarm_today,
                    timeInMillis.toFormatOnlyTime(context))
            in initTimeTomorrow until initTimeTomorrow + DAY_IN_MILLIS ->
                context.getString(R.string.text_alarm_tomorrow,
                    timeInMillis.toFormatOnlyTime(context))
            else -> timeInMillis.toFormat(context)
        }
    }

    fun calculateRangeInDays(context: Context, time: Pair<Long, Long>): String {
        val timeTotal = time.second - time.first
        val seconds: Long = timeTotal / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return context.resources.getQuantityString(
            R.plurals.days,
            days.toInt(), days
        )
    }
}