package com.nullpointer.noursecompose.domain.alarms

import com.nullpointer.noursecompose.models.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarmById(id: Long): Alarm?
    suspend fun insertAlarm(alarm: Alarm): Long
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun deleterAlarm(alarm: Alarm)
    suspend fun deleterListAlarm(list: List<Long>)
    suspend fun getAllAlarmActive():List<Alarm>
    fun getAllAlarms(): Flow<List<Alarm>>
}