package com.nullpointer.noursecompose.models.alarm

import androidx.annotation.StringRes
import com.nullpointer.noursecompose.R

enum class AlarmTypes(
    @StringRes
    val title: Int,
    @StringRes
    val description: Int,
) {
    ONE_SHOT(R.string.title_one_shot, R.string.description_one_shot),
    INDEFINITELY(R.string.title_indef,R.string.description_indef),
    RANGE(R.string.title_range, R.string.description_range)
}