package com.nullpointer.noursecompose.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.toBitmap
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepo: AlarmRepoImpl,
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