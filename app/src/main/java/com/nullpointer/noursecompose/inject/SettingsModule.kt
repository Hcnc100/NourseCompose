package com.nullpointer.noursecompose.inject

import android.content.Context
import com.nullpointer.noursecompose.data.local.datasource.pref.PrefDataSourceImpl
import com.nullpointer.noursecompose.data.local.datasource.pref.SettingsDataSource
import com.nullpointer.noursecompose.data.local.settings.MyDataStore
import com.nullpointer.noursecompose.domain.pref.SettingsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun providerPreferences(
        @ApplicationContext context: Context,
    ): MyDataStore = MyDataStore(context)

    @Provides
    @Singleton
    fun provideSettingsDataSource(
        dataStore: MyDataStore
    ): SettingsDataSource = PrefDataSourceImpl(dataStore)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        prefDataSource: SettingsDataSource
    ): SettingsRepoImpl = SettingsRepoImpl(prefDataSource)
}