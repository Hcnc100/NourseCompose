package com.nullpointer.noursecompose.inject

import android.content.Context
import com.nullpointer.noursecompose.data.local.datasource.pref.SettingsDataSource
import com.nullpointer.noursecompose.data.local.sound.SoundDataSource
import com.nullpointer.noursecompose.data.local.sound.SoundDataSourceImpl
import com.nullpointer.noursecompose.domain.sound.SoundRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SoundModule {

    @Provides
    @Singleton
    fun provideSoundDatSource(
        @ApplicationContext context: Context
    ): SoundDataSource = SoundDataSourceImpl(context)

    @Provides
    @Singleton
    fun provideSoundRepository(
        soundDataSource: SoundDataSource,
        settingsDataSource: SettingsDataSource
    ): SoundRepoImpl = SoundRepoImpl(soundDataSource, settingsDataSource)
}