package com.nullpointer.noursecompose.ui.screen

import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.util.toAndroidXPair
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.nullpointer.noursecompose.core.utils.getFirstTimeDay
import com.nullpointer.noursecompose.core.utils.getHourAndMinutesToday
import com.nullpointer.noursecompose.core.utils.setHourAndMinutesToday


class DialogDate {
    companion object {
        fun showDatePickerRange(
            activity: AppCompatActivity,
            updatedDate: (Pair<Long, Long>) -> Unit,
            currentSelect: Pair<Long, Long>? = null,
        ) {

            val picker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText("Selecona elm rango").apply {
                    setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointForward.now()).build()
                    )
                    currentSelect?.let {
                        setSelection(it.toAndroidXPair())
                    }
                }.build()
            picker.show(activity.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val (initTime, finishTime) = it
                updatedDate(Pair(getFirstTimeDay(initTime), getFirstTimeDay(finishTime)))
            }
        }

        fun showDatePickerOneDay(
            activity: AppCompatActivity,
            updatedDate: (Pair<Long, Long>) -> Unit,
            currentSelect: Pair<Long, Long>? = null,
        ) {

            val picker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Selecciona el dia de la alarma").apply {
                    setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointForward.now()).build()
                    )
                    currentSelect?.let {
                        setSelection(getFirstTimeDay())
                    }
                }.build()
            picker.show(activity.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                updatedDate(Pair(getFirstTimeDay(it), 0))
            }
        }

        fun showTimePicker(activity: AppCompatActivity, updatedDate: (Long) -> Unit) {
            val picker = MaterialTimePicker.Builder()
                .setTitleText("Select the start time")
                .apply {
                    // * get format system
                    val clockFormat = if (DateFormat.is24HourFormat(activity))
                        TimeFormat.CLOCK_24H
                    else
                        TimeFormat.CLOCK_12H
                    setTimeFormat(clockFormat)
                    // * set time now
                    val (hour, minutes) = getHourAndMinutesToday()
                    setHour(hour)
                    setMinute(minutes)
                }.build()
            picker.show(activity.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                // * get time change
                val timeInMillis = setHourAndMinutesToday(picker.hour, picker.minute)
                updatedDate(timeInMillis)
            }
        }
    }

}

