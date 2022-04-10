package com.nullpointer.noursecompose.models.measure

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.MeasureType.*

@Entity(tableName = "measure_table")
data class SimpleMeasure(
    val value: Float,
    val typeMeasure: MeasureType,
    val timestamp: Long = System.currentTimeMillis(),
):ItemSelected {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0

    @Ignore
    override var isSelected: Boolean = false
    fun toggleSelect() {
        this.isSelected = !this.isSelected
    }


    companion object {
        const val minValueTemp = 36
        const val maxValueTemp = 39

        const val minValueOxygen = 90
        const val maxValueOxygen = 100

        fun createFake(number: Int, typeMeasure: MeasureType): List<SimpleMeasure> {
            val (minValue, maxValue) = when (typeMeasure) {
                OXYGEN -> Pair(minValueOxygen, maxValueOxygen)
                TEMPERATURE -> Pair(minValueTemp, maxValueTemp)
            }
            val rangeInt = minValue..maxValue
            val rangeFloat = 0..100
            return (0..number).map {
                SimpleMeasure(
                    value = "${rangeInt.random()}.${rangeFloat.random()}".toFloat(),
                    typeMeasure
                ).apply {
                    id = it.toLong()
                }
            }
        }
    }
}