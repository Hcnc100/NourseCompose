package com.nullpointer.noursecompose.data.local.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nullpointer.noursecompose.models.notify.TypeNotify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyDataStore(
    private val context: Context,
) {
    companion object {
        private const val NAME_PREFERENCES = "settings"
        private const val KEY_TYPE_NOTIFY = "KEY_TYPE_NOTIFY"
        private const val KEY_INT_SOUND = "KEY_INT_SOUND"
    }

    private val Context.dataStore by preferencesDataStore(name = NAME_PREFERENCES)
    private val typeNotifyKey = stringPreferencesKey(KEY_TYPE_NOTIFY)
    private val intSoundKey = intPreferencesKey(KEY_INT_SOUND)

    val typeNotifyFlow: Flow<TypeNotify> = context.dataStore.data.map { preferences ->
        val name = preferences[typeNotifyKey]
        name?.let { TypeNotify.valueOf(it) } ?: TypeNotify.NOTIFY
    }

    val intSoundFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[intSoundKey] ?: -1
    }

    suspend fun changeTypeNotify(type: TypeNotify) {
        context.dataStore.edit { settings ->
            settings[typeNotifyKey] = type.name
        }
    }

    suspend fun changeSound(intSound: Int) {
        context.dataStore.edit { settings ->
            settings[intSoundKey] = intSound
        }
    }
}