package com.nullpointer.noursecompose.inject

import android.content.Context
import androidx.room.Room
import com.nullpointer.noursecompose.data.local.datasource.pref.PrefDataSource
import com.nullpointer.noursecompose.data.local.pref.MyDataStore
import com.nullpointer.noursecompose.data.local.room.NurseDatabase
import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.data.local.room.daos.LogsDAO
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.domain.registry.RegistryRepoImpl
import com.nullpointer.noursecompose.services.NotificationHelper
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
    fun provideNotifyHelper(
        @ApplicationContext context: Context,
    ): NotificationHelper = NotificationHelper(context)

}