package com.nullpointer.noursecompose.inject

import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.domain.alarms.AlarmRepository
import com.nullpointer.noursecompose.domain.compress.CompressImgRepoImpl
import com.nullpointer.noursecompose.domain.compress.CompressRepository
import com.nullpointer.noursecompose.domain.logger.LogsRepoImpl
import com.nullpointer.noursecompose.domain.logger.LogsRepository
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import com.nullpointer.noursecompose.domain.measure.MeasureRepository
import com.nullpointer.noursecompose.domain.pref.SettingsRepoImpl
import com.nullpointer.noursecompose.domain.pref.SettingsRepository
import com.nullpointer.noursecompose.domain.sound.SoundRepoImpl
import com.nullpointer.noursecompose.domain.sound.SoundRepository
import dagger.Binds
import dagger.Module
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
    ): MeasureRepository

    @Binds
    @Singleton
    abstract fun providePrefRepository(
        prefRepoImpl: SettingsRepoImpl
    ): SettingsRepository


    @Binds
    @Singleton
    abstract fun provideCompressRepository(
        compressImgRepoImpl: CompressImgRepoImpl
    ): CompressRepository

    @Binds
    @Singleton
    abstract fun provideSoundRepository(
        soundRepoImpl: SoundRepoImpl
    ): SoundRepository
}