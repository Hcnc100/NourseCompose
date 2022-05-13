package com.nullpointer.noursecompose.inject

import com.nullpointer.noursecompose.data.local.datasource.logs.LogsDataSource
import com.nullpointer.noursecompose.data.local.datasource.logs.LogsDataSourceImpl
import com.nullpointer.noursecompose.data.local.room.NurseDatabase
import com.nullpointer.noursecompose.data.local.room.daos.LogsDAO
import com.nullpointer.noursecompose.domain.measure.MeasureRepository
import com.nullpointer.noursecompose.domain.registry.RegistryRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LogModule {

    @Provides
    @Singleton
    fun provideRegistryDAO(database: NurseDatabase): LogsDAO =
        database.getLogDao()

    @Provides
    @Singleton
    fun provideLogsDataSource(logsDao: LogsDAO): LogsDataSource =
        LogsDataSourceImpl(logsDao)

    @Provides
    @Singleton
    fun provideLogsRepository(
        logsDataSource: LogsDataSource,
    ): RegistryRepoImpl = RegistryRepoImpl(logsDataSource)

}