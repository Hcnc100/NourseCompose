package com.nullpointer.noursecompose.inject

import com.nullpointer.noursecompose.data.local.datasource.measure.MeasureDataSource
import com.nullpointer.noursecompose.data.local.datasource.measure.MeasureDataSourceImpl
import com.nullpointer.noursecompose.data.local.room.NurseDatabase
import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MeasureModule {
    @Provides
    @Singleton
    fun provideMeasureDAO(database: NurseDatabase): MeasureDAO =
        database.getMeasureDAO()

    @Provides
    @Singleton
    fun provideMeasureDataSource(
        measureDAO: MeasureDAO
    ):MeasureDataSource= MeasureDataSourceImpl(measureDAO)

    @Provides
    @Singleton
    fun provideMeasureRepository(
        measureDataSource: MeasureDataSource
    ): MeasureRepoImpl = MeasureRepoImpl(measureDataSource)
}