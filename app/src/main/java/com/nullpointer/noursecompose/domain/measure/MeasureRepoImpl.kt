package com.nullpointer.noursecompose.domain.measure

import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

class MeasureRepoImpl(
    private val measureDAO: MeasureDAO
) :MeasureRepository{

    override fun getListTemp(): Flow<List<SimpleMeasure>> =
        measureDAO.getAllMeasureForType(MeasureType.TEMPERATURE)

    override  fun getListOxygen(): Flow<List<SimpleMeasure>> =
        measureDAO.getAllMeasureForType(MeasureType.OXYGEN)

    override suspend fun insertNewMeasure(measure: SimpleMeasure)=
        measureDAO.insert(measure)

    override suspend fun deleterMeasure(idMeasure:Long) =
        measureDAO.deleterMeasure(idMeasure)

    override suspend fun deleterListMeasure(listMeasure: List<Long>) =
        measureDAO.deleterListMeasure(listMeasure)

}