package com.nullpointer.noursecompose.data.local.datasource.alarm

import androidx.room.*
import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmDataSource {
    val listAlarms:Flow<List<Alarm>>
    suspend fun insert(item: Alarm): Long
    suspend fun deleterAlarm(item: Alarm)
    suspend fun updateAlarm(item: Alarm)
    suspend fun deleterAlarm(idDeleter: Int)
    suspend fun deleterListAlarm(listIdDeleter: List<Long>)
    suspend fun getAlarmById(id: Long): Alarm?
    fun getAllAlarmActive(): List<Alarm>
    fun getFlowAlarmActive(): Flow<List<Alarm>>
    fun getFlowAlarmInactive(): Flow<List<Alarm>>
    suspend fun getListAlarmForIds(listIds: List<Long>): List<Alarm>
}