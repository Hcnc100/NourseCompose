package com.nullpointer.noursecompose.inject

import android.content.Context
import androidx.room.Room
import com.nullpointer.noursecompose.data.local.NurseDatabase
import com.nullpointer.noursecompose.data.local.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.daos.MeasureDAO
import com.nullpointer.noursecompose.data.local.daos.RegistryDAO
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerUserDataBase(
        @ApplicationContext app: Context,
    ): NurseDatabase = Room.databaseBuilder(
        app,
        NurseDatabase::class.java,
        "NURSE_DATABASE"
    ).build()

    @Provides
    @Singleton
    fun providerAlarmDAO(database: NurseDatabase): AlarmDAO =
        database.getAlarmDAO()

    @Provides
    @Singleton
    fun provideRegistryDAO(database: NurseDatabase): RegistryDAO =
        database.getRegistryDao()

    @Provides
    @Singleton
    fun provideMeasureDAO(database: NurseDatabase): MeasureDAO =
        database.getMeasureDAO()

    @Provides
    @Singleton
    fun provideMeasureRepository(
        measureDAO: MeasureDAO
    ):MeasureRepoImpl = MeasureRepoImpl(measureDAO)
}