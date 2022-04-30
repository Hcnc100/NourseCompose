package com.nullpointer.noursecompose.domain.alarms

import android.content.Context
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.registry.Registry
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun addNewRegistry(newRegistry: Registry)
    suspend fun getAlarmById(id: Long): Alarm?
    suspend fun insertAlarm(alarm: Alarm,context: Context,isUpdate:Boolean): Long
    suspend fun updateAlarm(alarm: Alarm,context: Context)
    suspend fun deleterAlarm(alarm: Alarm,context: Context)
    suspend fun restoreAlarm(alarm: Alarm,context: Context)
    suspend fun deleterListAlarm(list: List<Long>,context: Context)
    fun getAllAlarmActive():List<Alarm>
    fun getAllAlarms(): Flow<List<Alarm>>
    suspend fun getAlarmsForIds(listIds:List<Long>):List<Alarm>
}