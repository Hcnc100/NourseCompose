package com.nullpointer.noursecompose.data.local.datasource.pref

import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    val typeNotifyFlow: Flow<TypeNotify>
    val intSoundFlow: Flow<Int>
    suspend fun changeTypeNotify(type: TypeNotify)
    suspend fun changeSound(intSound: Int)
}