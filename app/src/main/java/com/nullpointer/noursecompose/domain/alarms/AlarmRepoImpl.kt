package com.nullpointer.noursecompose.domain.alarms

import com.nullpointer.noursecompose.data.local.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.daos.RegistryDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.registry.Registry
import com.nullpointer.noursecompose.models.registry.TypeRegistry
import kotlinx.coroutines.flow.Flow

class AlarmRepoImpl(
    private val alarmDAO: AlarmDAO,
    private val registryDAO: RegistryDAO
) : AlarmRepository  {
    override suspend fun addNewRegistry(newRegistry: Registry) =
        registryDAO.insertRegistry(newRegistry)

    override suspend fun getAlarmById(id: Long): Alarm? =
        alarmDAO.getAlarmById(id)

    override suspend fun insertAlarm(alarm: Alarm):Long{
        val idAlarm=alarmDAO.insert(alarm)
        registryDAO.insertRegistry(Registry(TypeRegistry.CREATE,idAlarm))
        return idAlarm
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDAO.updateAlarm(alarm)
        registryDAO.insertRegistry(Registry(TypeRegistry.UPDATE,alarm.id))
    }

    override suspend fun deleterAlarm(alarm: Alarm) {
        alarmDAO.deleterAlarm(alarm)
        registryDAO.insertRegistry(Registry(TypeRegistry.DELETER,alarm.id))
    }

    override suspend fun deleterListAlarm(list: List<Long>) =
        alarmDAO.deleterListAlarm(list)

    override suspend fun getAllAlarmActive(): List<Alarm> =
        alarmDAO.getAllAlarmActive()

    override fun getAllAlarms(): Flow<List<Alarm>> =
        alarmDAO.getAllAlarms()
}