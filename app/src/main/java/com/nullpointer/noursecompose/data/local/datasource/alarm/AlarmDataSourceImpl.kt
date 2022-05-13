package com.nullpointer.noursecompose.data.local.datasource.alarm

import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmDataSourceImpl(
    private val alarmDAO: AlarmDAO,
) : AlarmDataSource {
    override val listAlarms: Flow<List<Alarm>> =
        alarmDAO.getAllAlarms()

    override suspend fun insert(item: Alarm): Long =
        alarmDAO.insert(item)

    override suspend fun deleterAlarm(item: Alarm) =
        alarmDAO.deleterAlarm(item)

    override suspend fun deleterAlarm(idDeleter: Int) =
        alarmDAO.deleterAlarm(idDeleter)

    override suspend fun updateAlarm(item: Alarm) =
        alarmDAO.updateAlarm(item)

    override suspend fun deleterListAlarm(listIdDeleter: List<Long>) =
        alarmDAO.deleterListAlarm(listIdDeleter)

    override suspend fun getAlarmById(id: Long): Alarm? =
        alarmDAO.getAlarmById(id)

    override fun getAllAlarmActive(): List<Alarm> =
        alarmDAO.getAllAlarmActive()

    override fun getFlowAlarmActive(): Flow<List<Alarm>> =
        alarmDAO.getFlowAlarmActive()

    override fun getFlowAlarmInactive(): Flow<List<Alarm>> =
        alarmDAO.getFlowAlarmInactive()

    override suspend fun getListAlarmForIds(listIds: List<Long>): List<Alarm> =
        alarmDAO.getListAlarmForIds(listIds)
}