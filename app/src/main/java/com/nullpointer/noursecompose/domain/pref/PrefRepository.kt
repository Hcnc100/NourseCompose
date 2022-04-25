package com.nullpointer.noursecompose.domain.pref

import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

interface PrefRepository {
    val typeNotify: Flow<TypeNotify>
    val intSound:Flow<Int>
    suspend fun changeTypeNotify(type:TypeNotify)
    suspend fun changeIntSound(intSound:Int)
}