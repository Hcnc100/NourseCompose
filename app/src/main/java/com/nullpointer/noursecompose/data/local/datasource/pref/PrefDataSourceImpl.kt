package com.nullpointer.noursecompose.data.local.datasource.pref

import com.nullpointer.noursecompose.data.local.settings.MyDataStore
import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow

class PrefDataSourceImpl(
    private val myDataStore: MyDataStore,
) : SettingsDataSource {
    override val typeNotifyFlow: Flow<TypeNotify> =
        myDataStore.typeNotifyFlow

    override val intSoundFlow: Flow<Int> =
        myDataStore.intSoundFlow

    override suspend fun changeTypeNotify(type: TypeNotify) =
        myDataStore.changeTypeNotify(type)

    override suspend fun changeSound(intSound: Int) =
        myDataStore.changeSound(intSound)
}