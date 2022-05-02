package com.nullpointer.noursecompose.ui.screen.config.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.domain.pref.PrefRepoImpl
import com.nullpointer.noursecompose.models.notify.TypeNotify
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val prefRepo: PrefRepoImpl,
) : ViewModel() {

    val typeNotify = prefRepo.typeNotify.catch {
        Timber.e("Error al obtener el tipo de las notificaciones $it")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TypeNotify.NOTIFY
    )



    val intSound = prefRepo.intSound.catch {
        Timber.e("Error al obtener el sonido $it")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        -1
    )




    fun changeTypeNotify(type: TypeNotify) = viewModelScope.launch(Dispatchers.IO) {
        prefRepo.changeTypeNotify(type)
    }

    fun changeIntSound(intSound: Int) = viewModelScope.launch(Dispatchers.IO) {
        prefRepo.changeIntSound(intSound)
    }
}