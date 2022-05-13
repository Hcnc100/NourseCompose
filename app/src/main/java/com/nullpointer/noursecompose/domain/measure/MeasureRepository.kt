package com.nullpointer.noursecompose.domain.measure

import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

interface MeasureRepository {
    val listTemp:Flow<List<SimpleMeasure>>
    val listOxygen:Flow<List<SimpleMeasure>>
    suspend fun insertNewMeasure(measure: SimpleMeasure):Long
    suspend fun deleterMeasure(idMeasure:Long)
    suspend fun deleterListMeasure(listMeasure:List<Long>)
}