package com.nullpointer.noursecompose.data.local.sound

import kotlinx.coroutines.flow.Flow

interface SoundDataSource {
    val isPlaying: Flow<Boolean>
    val listSoundRaw: List<Int>
    val isPlayInLoop: Flow<Boolean>
    fun releaseSound()
    fun togglePlayPause()
    fun stopSoundInLoop()
    fun changeSong(indexSound: Int)
    fun startSoundInLoop()
}