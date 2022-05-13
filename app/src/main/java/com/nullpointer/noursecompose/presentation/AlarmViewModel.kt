package com.nullpointer.noursecompose.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.toBitmap
import com.nullpointer.noursecompose.domain.alarms.AlarmRepoImpl
import com.nullpointer.noursecompose.models.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepo: AlarmRepoImpl,
) : ViewModel() {


    val listAlarm = alarmRepo.listAlarms.catch {
        Timber.e("Error al cargar las alarmas de la base de datos $it")
        emit(emptyList())
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    fun addNewAlarm(
        alarm: Alarm,
        file: File?,
        nameFile: String?,
        context: Context,
    ) = viewModelScope.launch(Dispatchers.IO) {
        // * saved img in internal storage
        val nameImgSaved = file?.let {
            ImageUtils.saveToInternalStorage(
                bitmapImage = file.toBitmap(),
                nameFile = "alarm-img-${System.currentTimeMillis()}.png",
                context = context
            )
        } ?: nameFile

        // * if update the image so, deleter the last image
        alarm.nameFile?.let {
            ImageUtils.deleterImgFromStorage(it, context)
        }

        // * if the name is not null update alarm to save
        val alarmInsert = if (nameImgSaved != null) alarm.copy(nameFile = nameImgSaved) else alarm
        alarmRepo.insertAlarm(alarmInsert, context, alarm.id != null)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleterAlarm(alarm: Alarm, context: Context) = viewModelScope.launch(Dispatchers.IO) {
        alarmRepo.deleterAlarm(alarm, context)
    }

    fun deleterListAlarm(listIds: List<Long>, context: Context) =
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepo.deleterListAlarm(listIds, context)
        }

    suspend fun getAlarmById(id: Long): Alarm? {
        return alarmRepo.getAlarmById(id)
    }

}