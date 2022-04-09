package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.domain.measure.MeasureRepoImpl
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
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
class MeasureViewModel @Inject constructor(
    private val measureRepo: MeasureRepoImpl,
) : ViewModel() {

    val listOxygen = measureRepo.getListOxygen().catch {
        Timber.e("Error al obtener la lista de oxigeno $it")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    val listTemp = measureRepo.getListTemp().catch {
        Timber.e("Error al obtener la lista de temp $it")
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    fun addNewMeasure(simpleMeasure: SimpleMeasure) = viewModelScope.launch(Dispatchers.IO){
        measureRepo.insertNewMeasure(simpleMeasure)
    }

    fun deleterListMeasure() = viewModelScope.launch(Dispatchers.IO) {

    }

    fun deleterMeasure() = viewModelScope.launch(Dispatchers.IO) {

    }
}