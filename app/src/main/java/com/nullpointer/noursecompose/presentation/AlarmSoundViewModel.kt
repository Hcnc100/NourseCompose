package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.domain.sound.SoundRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlarmSoundViewModel @Inject constructor(
    soundRepository: SoundRepository
) : ViewModel() {

    val isAlarmSound = soundRepository.isPlayingInLoop.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        true
    )
}