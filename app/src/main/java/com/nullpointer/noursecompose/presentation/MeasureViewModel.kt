package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.domain.measure.MeasureRepository
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MeasureViewModel @Inject constructor(
    private val measureRepo: MeasureRepository
) : ViewModel() {

    val listOxygen = flow<Resource<List<SimpleMeasure>>> {
        measureRepo.listOxygen.collect {
            emit(Resource.Success(it))
        }
    }.catch {
        Timber.e("Error get list oxygen from db $it")
        emit(Resource.Failure)
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Resource.Loading
    )

    val listTemp = flow<Resource<List<SimpleMeasure>>> {
        measureRepo.listTemp.collect {
            emit(Resource.Success(it))
        }
    }.catch {
        Timber.e("Error get list temp from db $it")
        emit(Resource.Failure)
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Resource.Loading
    )

    fun addNewMeasure(
        simpleMeasure: SimpleMeasure,
    ) = viewModelScope.launch(Dispatchers.IO) {
        measureRepo.insertNewMeasure(simpleMeasure)
    }

    fun deleterListMeasure(
        listIdMeasure: List<Long>
    ) = viewModelScope.launch(Dispatchers.IO) {
        measureRepo.deleterListMeasure(listIdMeasure)
    }

    fun deleterMeasure(
        idMeasure: Long
    ) = viewModelScope.launch(Dispatchers.IO) {
        measureRepo.deleterMeasure(idMeasure)
    }
}