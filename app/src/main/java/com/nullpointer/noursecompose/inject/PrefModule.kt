package com.nullpointer.noursecompose.inject

import android.content.Context
import com.nullpointer.noursecompose.data.local.datasource.pref.PrefDataSource
import com.nullpointer.noursecompose.data.local.datasource.pref.PrefDataSourceImpl
import com.nullpointer.noursecompose.data.local.pref.MyDataStore
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.domain.pref.PrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefModule {
    @Provides
    @Singleton
    fun providerPreferences(
        @ApplicationContext context: Context,
    ): MyDataStore = MyDataStore(context)

    @Provides
    @Singleton
    fun providePrefDataSource(
        dataStore:MyDataStore
    ):PrefDataSource= PrefDataSourceImpl(dataStore)

    @Provides
    @Singleton
    fun providePreferenceRepository(
        prefDataSource: PrefDataSource
    ): PrefRepoImpl = PrefRepoImpl(prefDataSource)
}