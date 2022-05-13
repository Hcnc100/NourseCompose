package com.nullpointer.noursecompose.inject

import com.nullpointer.noursecompose.data.local.datasource.alarm.AlarmDataSource
import com.nullpointer.noursecompose.data.local.datasource.alarm.AlarmDataSourceImpl
import com.nullpointer.noursecompose.data.local.datasource.logs.LogsDataSource
import com.nullpointer.noursecompose.data.local.room.NurseDatabase
import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.room.daos.LogsDAO
import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideMeasureDAO(database: NurseDatabase): AlarmDAO =
        database.getAlarmDAO()

    @Provides
    @Singleton
    fun provideAlarmDataSource(
        alarmDAO: AlarmDAO,
    ): AlarmDataSource = AlarmDataSourceImpl(alarmDAO)

    @Provides
    @Singleton
    fun providerAlarmRepository(
        alarmDataSource: AlarmDataSource,
        logsDataSource: LogsDataSource
    ): AlarmRepoImpl = AlarmRepoImpl(alarmDataSource, logsDataSource)
}