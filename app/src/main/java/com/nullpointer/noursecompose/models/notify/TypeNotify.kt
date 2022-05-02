package com.nullpointer.noursecompose.models.notify

import androidx.annotation.StringRes
import com.nullpointer.noursecompose.R

enum class TypeNotify(
    @StringRes
    val title: Int,
    @StringRes
    val description: Int) {
    NOTIFY(R.string.title_notify, R.string.description_notify),
    ALARM(R.string.title_alarm, R.string.description_lauch_alarm)
}