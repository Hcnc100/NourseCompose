package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.domain.registry.RegistryRepoImpl
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.models.registry.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val registryRepoImpl: RegistryRepoImpl,
) : ViewModel() {

    val listLogs = flow<Resource<List<Log>>> {
        registryRepoImpl.listLogs.collect{
            emit(Resource.Success(it))
        }
    }.catch {
        Timber.e("Error get logs $it")
        emit(Resource.Failure)
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Resource.Loading
    )

    fun deleterAllRegistry() = viewModelScope.launch(Dispatchers.IO) {
        registryRepoImpl.deleterAllLogs()
    }

    fun deleterRegistry(
        listIds: List<Long>,
    ) = viewModelScope.launch(Dispatchers.IO) {
        registryRepoImpl.deleterLogs(listIds)
    }
}