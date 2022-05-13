package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.domain.registry.RegistryRepoImpl
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
class LogViewModel @Inject constructor(
    private val registryRepoImpl: RegistryRepoImpl,
) : ViewModel() {

    val listLogs = registryRepoImpl.listLogs.catch {
        Timber.e("Error al obtener los logs de la base de datos")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    fun deleterAllRegistry() = viewModelScope.launch(Dispatchers.IO) {
        registryRepoImpl.deleterAllLogs()
    }
}