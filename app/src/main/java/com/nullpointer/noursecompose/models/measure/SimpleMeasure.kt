package com.nullpointer.noursecompose.models.measure

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.nullpointer.noursecompose.models.ItemSelected

@Entity(tableName = "measure_table")
data class SimpleMeasure(
    val value: Float,
    val typeMeasure: MeasureType,
    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
) : ItemSelected {

    @Ignore
    override var isSelected: Boolean = false


    companion object {
        fun createFake(number: Int, typeMeasure: MeasureType): List<SimpleMeasure> {
            return (0..number).map {
                SimpleMeasure(
                    value = typeMeasure.getRandomValue(),
                    typeMeasure = typeMeasure,
                    id = it.toLong()
                )
            }
        }
    }
}