package com.nullpointer.noursecompose.domain.measure

import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

interface MeasureRepository {
    fun getListTemp():Flow<List<SimpleMeasure>>
    fun getListOxygen():Flow<List<SimpleMeasure>>
    suspend fun insertNewMeasure(measure: SimpleMeasure):Long
    suspend fun deleterMeasure(measure: SimpleMeasure)
    suspend fun deleterListMeasure(listMeasure:List<Long>)
}