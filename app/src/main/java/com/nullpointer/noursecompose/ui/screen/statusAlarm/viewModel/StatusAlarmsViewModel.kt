package com.nullpointer.noursecompose.ui.screen.statusAlarm.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusAlarmsViewModel @Inject constructor(
    private val alarmRepoImpl: AlarmRepoImpl,
) : ViewModel() {

    val listIdsAlarm = mutableStateOf<List<Alarm>?>(null)

    private var jobGet: Job? = null

    fun initAlarms(listIds: List<Long>) {
        jobGet?.cancel()
        jobGet = viewModelScope.launch {
            listIdsAlarm.value = alarmRepoImpl.getAlarmsForIds(listIds)
        }
    }


}