package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.launchSafeIO
import com.nullpointer.noursecompose.domain.measure.MeasureRepository
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MeasureViewModel @Inject constructor(
    private val measureRepo: MeasureRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_IS_SHOW_DIALOG = "KEY_IS_SHOW_DIALOG"
        private const val KEY_MEASURE_INPUT = "KEY_MEASURE_INPUT"
    }

    var isShowDialogAddMeasure by SavableComposeState(savedStateHandle, KEY_IS_SHOW_DIALOG, false)
        private set

    var measureInputProperty: PropertySavableString? = null

    fun showDialogInputMeasure(measureType: MeasureType) {
        measureInputProperty?.clearValue()
        measureInputProperty = PropertySavableString(
            maxLength = 5,
            tagSavable = KEY_MEASURE_INPUT,
            hint = R.string.title_new_measure,
            label = measureType.nameMeasure,
            savedState = savedStateHandle,
            emptyError = R.string.message_error_measure_empty,
            lengthError = R.string.message_error_measure_length
        )
        isShowDialogAddMeasure = true
    }

    fun hiddeDialogInputMeasure() {
        isShowDialogAddMeasure = false
    }


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
        type: MeasureType,
        callbackSuccess: () -> Unit
    ) = launchSafeIO {
        measureInputProperty?.let { measureInputProperty ->
            measureInputProperty.reValueField()
            if (!measureInputProperty.hasError) {
                val newMeasure = measureInputProperty.currentValue.toFloatOrNull() ?: 0F
                if (newMeasure in type.minValue..type.maxValue) {
                    measureRepo.insertNewMeasure(SimpleMeasure(newMeasure, type))
                    withContext(Dispatchers.Main) {
                        isShowDialogAddMeasure = false
                        callbackSuccess()
                    }
                } else {
                    measureInputProperty.setAnotherError(R.string.message_error_measure_value)
                }
            }
        }
    }

    fun deleterListMeasure(
        listIdMeasure: List<Long>
    ) = launchSafeIO {
        measureRepo.deleterListMeasure(listIdMeasure)
    }

    fun deleterMeasure(
        idMeasure: Long
    ) = launchSafeIO {
        measureRepo.deleterMeasure(idMeasure)
    }
}