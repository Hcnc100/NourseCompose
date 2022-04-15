package com.nullpointer.noursecompose.domain.alarms

import com.nullpointer.noursecompose.data.local.daos.AlarmDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmRepoImpl(
    private val alarmDAO: AlarmDAO,
) : AlarmRepository  {
    override suspend fun getAlarmById(id: Long): Alarm? =
        alarmDAO.getAlarmById(id)

    override suspend fun insertAlarm(alarm: Alarm) =
        alarmDAO.insert(alarm)

    override suspend fun updateAlarm(alarm: Alarm) =
        alarmDAO.updateAlarm(alarm)

    override suspend fun deleterAlarm(alarm: Alarm) =
        alarmDAO.deleterAlarm(alarm)

    override suspend fun deleterListAlarm(list: List<Long>) =
        alarmDAO.deleterListAlarm(list)

    override suspend fun getAllAlarmActive(): List<Alarm> =
        alarmDAO.getAllAlarmActive()

    override fun getAllAlarms(): Flow<List<Alarm>> =
        alarmDAO.getAllAlarms()
}