package com.nullpointer.noursecompose.domain.pref

import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val typeNotify: Flow<TypeNotify>
    val intSound: Flow<Int>
    val isAlarmSound: Flow<Boolean>
    fun changeIsAlarmSound(isAlarmSound: Boolean)
    suspend fun changeTypeNotify(type: TypeNotify)
    suspend fun changeIntSound(intSound: Int)
}