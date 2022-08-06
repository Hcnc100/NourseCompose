package com.nullpointer.noursecompose.inject

import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.domain.alarms.AlarmRepository
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import com.nullpointer.noursecompose.domain.measure.MeasureRepository
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.domain.pref.PrefRepository
import com.nullpointer.noursecompose.domain.registry.LogsRepoImpl
import com.nullpointer.noursecompose.domain.registry.LogsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAlarmRepository(
        alarmRepoImpl: AlarmRepoImpl
    ):AlarmRepository

    @Binds
    @Singleton
    abstract fun provideLogsRepository(
        logsRepoImpl: LogsRepoImpl
    ):LogsRepository

    @Binds
    @Singleton
    abstract fun provideMeasureRepository(
        measureRepoImpl: MeasureRepoImpl
    ):MeasureRepository

    @Binds
    @Singleton
    abstract fun providePrefRepository(
        prefRepoImpl: PrefRepoImpl
    ):PrefRepository
}