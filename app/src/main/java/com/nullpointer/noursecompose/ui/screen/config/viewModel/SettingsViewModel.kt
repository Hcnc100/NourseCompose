package com.nullpointer.noursecompose.ui.screen.config.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.domain.sound.SoundRepository
import com.nullpointer.noursecompose.models.notify.TypeNotify
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val soundRepository: SoundRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            soundRepository.prepareSoundSaved()
        }
    }

    val typeNotify = soundRepository.typeNotify.catch {
        Timber.e("Error al obtener el tipo de las notificaciones $it")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TypeNotify.NOTIFY
    )

    val isPlaying = soundRepository.isPlaying.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )

    val listSoundSize get() = soundRepository.listSoundRaw.size


    val intSound = soundRepository.indexSound.catch {
        Timber.e("Error al obtener el sonido $it")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        -1
    )

    fun togglePlayPauseSound() = viewModelScope.launch(Dispatchers.IO) {
        soundRepository.togglePlayPause()
    }


    fun changeTypeNotify(type: TypeNotify) = viewModelScope.launch(Dispatchers.IO) {
        soundRepository.changeTypeNotify(type)
        if (soundRepository.isPlaying.first() && !soundRepository.isPlayingInLoop.first()) {
            soundRepository.togglePlayPause()
        }
    }

    fun changeIntSound(intSound: Int) = viewModelScope.launch(Dispatchers.IO) {
        soundRepository.changeSound(intSound)
    }

    override fun onCleared() {
        super.onCleared()
        soundRepository.releaseSound()
    }
}