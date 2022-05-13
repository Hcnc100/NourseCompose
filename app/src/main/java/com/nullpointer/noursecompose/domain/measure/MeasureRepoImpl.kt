package com.nullpointer.noursecompose.domain.measure

import com.nullpointer.noursecompose.data.local.datasource.measure.MeasureDataSource
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

class MeasureRepoImpl(
    private val measureDataSource: MeasureDataSource
) :MeasureRepository{

    override val listTemp: Flow<List<SimpleMeasure>> = measureDataSource.listTemp
    override val listOxygen: Flow<List<SimpleMeasure>> = measureDataSource.listOxygen

    override suspend fun insertNewMeasure(measure: SimpleMeasure)=
        measureDataSource.insert(measure)

    override suspend fun deleterMeasure(idMeasure:Long) =
        measureDataSource.deleterMeasure(idMeasure)

    override suspend fun deleterListMeasure(listMeasure: List<Long>) =
        measureDataSource.deleterListMeasure(listMeasure)

}