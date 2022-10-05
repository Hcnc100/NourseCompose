package com.nullpointer.noursecompose.domain.sound

import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

interface SoundRepository {
    val indexSound: Flow<Int>
    val listSoundRaw: List<Int>
    val isPlaying: Flow<Boolean>
    val typeNotify: Flow<TypeNotify>
    val isPlayingInLoop: Flow<Boolean>
    fun releaseSound()
    fun togglePlayPause()
    suspend fun changeSound(indexSound: Int)
    fun changeIsAlarmSound(isAlarmSound: Boolean)
    suspend fun changeTypeNotify(type: TypeNotify)
    suspend fun prepareSoundSaved()
    suspend fun startSoundInLoopAlarm()
    suspend fun stopSoundInLoopAlarm()
}