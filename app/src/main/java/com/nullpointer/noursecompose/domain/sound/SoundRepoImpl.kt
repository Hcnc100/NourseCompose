package com.nullpointer.noursecompose.domain.sound

import com.nullpointer.noursecompose.data.local.datasource.pref.SettingsDataSource
import com.nullpointer.noursecompose.data.local.sound.SoundDataSource
import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SoundRepoImpl(
    private val soundDataSource: SoundDataSource,
    private val settingsDataSource: SettingsDataSource
) : SoundRepository {

    override val isPlaying: Flow<Boolean> = soundDataSource.isPlaying

    override val listSoundRaw: List<Int> = soundDataSource.listSoundRaw

    override val typeNotify: Flow<TypeNotify> = settingsDataSource.typeNotifyFlow

    override val indexSound: Flow<Int> = settingsDataSource.intSoundFlow

    override val isPlayingInLoop: Flow<Boolean> = soundDataSource.isPlayInLoop

    override suspend fun prepareSoundSaved() {
        // * get index sound saved
        val indexSoundSaved = settingsDataSource.intSoundFlow.first()
        // * and prepare the media player
        soundDataSource.changeSong(indexSoundSaved)
    }


    override fun togglePlayPause() =
        soundDataSource.togglePlayPause()

    override fun changeIsAlarmSound(isAlarmSound: Boolean) {

    }

    override suspend fun changeTypeNotify(type: TypeNotify) =
        settingsDataSource.changeTypeNotify(type)

    override suspend fun changeSound(indexSound: Int) {
        settingsDataSource.changeSound(indexSound)
        soundDataSource.changeSong(indexSound)
    }

    override fun releaseSound() {
        soundDataSource.releaseSound()
    }

    override suspend fun startSoundInLoopAlarm() {
        prepareSoundSaved()
        soundDataSource.startSoundInLoop()
    }

    override suspend fun stopSoundInLoopAlarm() {
        soundDataSource.stopSoundInLoop()
    }
}