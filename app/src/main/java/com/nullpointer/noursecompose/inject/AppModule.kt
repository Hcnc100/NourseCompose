package com.nullpointer.noursecompose.inject

import android.content.Context
import androidx.room.Room
import com.nullpointer.noursecompose.data.local.pref.Preferences
import com.nullpointer.noursecompose.data.local.room.NurseDatabase
import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.data.local.room.daos.RegistryDAO
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.domain.registry.RegistryRepoImpl
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

    @Provides
    @Singleton
    fun provideRegistryRepository(
        registryDAO: RegistryDAO
    ):RegistryRepoImpl =RegistryRepoImpl(registryDAO)


    @Provides
    @Singleton
    fun providerAlarmRepository(
        alarmDAO: AlarmDAO,
        registryDAO: RegistryDAO
    ): AlarmRepoImpl = AlarmRepoImpl(alarmDAO, registryDAO)

    @Provides
    @Singleton
    fun providerPreferences(
        @ApplicationContext context: Context
    )=Preferences(context)

    @Provides
    @Singleton
    fun providePreferenceRepository(
        preferences: Preferences
    ):PrefRepoImpl= PrefRepoImpl(preferences)
}