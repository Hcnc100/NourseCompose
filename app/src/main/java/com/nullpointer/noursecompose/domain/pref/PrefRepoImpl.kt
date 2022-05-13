package com.nullpointer.noursecompose.domain.pref

import com.nullpointer.noursecompose.data.local.datasource.pref.PrefDataSource
import com.nullpointer.noursecompose.data.local.pref.MyDataStore
import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

class PrefRepoImpl(
    private val prefDataSource: PrefDataSource
) : PrefRepository {
    override val typeNotify: Flow<TypeNotify> =
        prefDataSource.typeNotifyFlow

    override val intSound: Flow<Int> =
        prefDataSource.intSoundFlow

    override suspend fun changeTypeNotify(type: TypeNotify) =
        prefDataSource.changeTypeNotify(type)

    override suspend fun changeIntSound(intSound: Int) =
        prefDataSource.changeSound(intSound)
}