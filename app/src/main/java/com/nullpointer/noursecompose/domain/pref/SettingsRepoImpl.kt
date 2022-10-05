package com.nullpointer.noursecompose.domain.pref

import com.nullpointer.noursecompose.data.local.datasource.pref.SettingsDataSource
import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsRepoImpl(
    private val prefDataSource: SettingsDataSource
) : SettingsRepository {
    override val typeNotify: Flow<TypeNotify> =
        prefDataSource.typeNotifyFlow

    override val intSound: Flow<Int> =
        prefDataSource.intSoundFlow

    private val _isAlarmSound = MutableStateFlow(false)

    override val isAlarmSound: Flow<Boolean> = _isAlarmSound

    override fun changeIsAlarmSound(isAlarmSound: Boolean) {
        _isAlarmSound.value = isAlarmSound
    }

    override suspend fun changeTypeNotify(type: TypeNotify) =
        prefDataSource.changeTypeNotify(type)

    override suspend fun changeIntSound(intSound: Int) =
        prefDataSource.changeSound(intSound)
}