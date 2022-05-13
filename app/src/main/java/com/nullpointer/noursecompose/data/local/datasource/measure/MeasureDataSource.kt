package com.nullpointer.noursecompose.data.local.datasource.measure

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

interface MeasureDataSource {
    val listTemp:Flow<List<SimpleMeasure>>
    val listOxygen:Flow<List<SimpleMeasure>>
    suspend fun insert(measure: SimpleMeasure): Long
    suspend fun deleterMeasure(idMeasure: Long)
    suspend fun deleterListMeasure(listIdDeleter: List<Long>)
}