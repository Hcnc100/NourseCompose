package com.nullpointer.noursecompose.models.measure

import com.nullpointer.noursecompose.R

enum class MeasureType(
    val minValue: Float,
    val maxValue: Float,
    val suffix: Int,
    val descriptionAdd: Int,
    val descriptionRemove: Int,
    val nameMeasure: Int,
    val animationEmpty: Int,
    val textEmpty: Int,
) {
    OXYGEN(
        minValue = 90F,
        maxValue = 100F,
        animationEmpty = R.raw.empty2,
        suffix = R.string.suffix_oxygen,
        nameMeasure = R.string.name_oxygen,
        textEmpty = R.string.message_empty_oxygen,
        descriptionAdd = R.string.description_add_oxygen,
        descriptionRemove = R.string.description_remove_oxygen
    ),
    TEMPERATURE(
        minValue = 36F,
        maxValue = 39F,
        suffix = R.string.suffix_temp,
        animationEmpty = R.raw.empty1,
        nameMeasure = R.string.name_temp,
        textEmpty = R.string.message_empty_temp,
        descriptionAdd = R.string.description_add_temp,
        descriptionRemove = R.string.description_remove_temp
    );

    fun getRandomValue(): Float {
        val rangeInt = (minValue.toInt()..maxValue.toInt())
        val rangeFloat = (0..99)
        return "${rangeInt.random()}.${rangeFloat.random()}".toFloat()
    }
}
