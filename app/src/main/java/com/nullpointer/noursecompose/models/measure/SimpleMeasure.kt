package com.nullpointer.noursecompose.models.measure

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "measure_table")
data class SimpleMeasure(
    val value:Float,
    val typeMeasure: MeasureType,
    val timestamp:Long=System.currentTimeMillis(),
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = -1

    @Ignore
    var isSelect:Boolean=false
    fun toggleSelect() {
        this.isSelect=!this.isSelect
    }
}