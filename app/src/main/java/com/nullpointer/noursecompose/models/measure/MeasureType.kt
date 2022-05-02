package com.nullpointer.noursecompose.models.measure

import androidx.annotation.StringRes
import com.nullpointer.noursecompose.R

enum class MeasureType(
    val minValue: Float,
    val maxValue: Float,
) {
    OXYGEN(90F, 100F),
    TEMPERATURE(36F, 39F);

    fun getRandomValue(): Float {
        val rangeInt = (minValue.toInt()..maxValue.toInt())
        val rangeFloat = (0..99)
        return "${rangeInt.random()}.${rangeFloat.random()}".toFloat()
    }
}
