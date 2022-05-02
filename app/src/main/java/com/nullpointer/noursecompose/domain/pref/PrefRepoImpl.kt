package com.nullpointer.noursecompose.domain.pref

import com.nullpointer.noursecompose.data.local.pref.Preferences
import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

class PrefRepoImpl(
    private val preferences: Preferences,
) : PrefRepository {
    override val typeNotify: Flow<TypeNotify> =
        preferences.typeNotifyFlow

    override val intSound: Flow<Int> =
        preferences.intSoundFlow

    override suspend fun changeTypeNotify(type: TypeNotify) =
        preferences.changeTypeNotify(type)

    override suspend fun changeIntSound(intSound: Int) =
        preferences.changeSound(intSound)
}