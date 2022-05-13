package com.nullpointer.noursecompose.data.local.datasource.measure

import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

class MeasureDataSourceImpl(
    private val measureDAO: MeasureDAO,
) : MeasureDataSource {
    override val listTemp: Flow<List<SimpleMeasure>> =
        measureDAO.getAllMeasureForType(MeasureType.TEMPERATURE)

    override val listOxygen: Flow<List<SimpleMeasure>> =
        measureDAO.getAllMeasureForType(MeasureType.OXYGEN)

    override suspend fun insert(measure: SimpleMeasure): Long =
        measureDAO.insert(measure)

    override suspend fun deleterMeasure(idMeasure: Long) =
        measureDAO.deleterMeasure(idMeasure)

    override suspend fun deleterListMeasure(listIdDeleter: List<Long>) =
        measureDAO.deleterListMeasure(listIdDeleter)

}