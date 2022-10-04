package com.nullpointer.noursecompose.domain.alarms

import android.net.Uri
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    val listAlarms:Flow<List<Alarm>>
    suspend fun addNewLog(log: Log)
    suspend fun getAlarmById(id: Long): Alarm?
    suspend fun insertAlarm(alarm: Alarm, uriImg: Uri?): Long
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun deleterAlarm(alarm: Alarm)
    suspend fun restoreAlarm(alarm: Alarm)
    suspend fun deleterListAlarm(list: List<Long>)
    fun getAllAlarmActive():List<Alarm>
    suspend fun getAlarmsForIds(listIds:List<Long>):List<Alarm>
}