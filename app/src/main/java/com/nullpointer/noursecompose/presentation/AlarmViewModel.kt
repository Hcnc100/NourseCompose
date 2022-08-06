package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.domain.alarms.AlarmRepository
import com.nullpointer.noursecompose.models.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepo: AlarmRepository
) : ViewModel() {

    val listAlarm = flow<Resource<List<Alarm>>> {
        alarmRepo.listAlarms.collect{
            emit(Resource.Success(it))
        }
    }.catch {
        Timber.e("Error load list alarm to database $it")
        emit(Resource.Failure)
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Resource.Loading
    )

    fun addNewAlarm(
        alarm: Alarm,
        uriImg:String?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        alarmRepo.insertAlarm(alarm,uriImg)
    }

    fun deleterAlarm(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        alarmRepo.deleterAlarm(alarm)
    }

    fun deleterListAlarm(listIds: List<Long>) = viewModelScope.launch(Dispatchers.IO) {
            alarmRepo.deleterListAlarm(listIds)
        }

    suspend fun getAlarmById(id: Long): Alarm? {
        return alarmRepo.getAlarmById(id)
    }

}