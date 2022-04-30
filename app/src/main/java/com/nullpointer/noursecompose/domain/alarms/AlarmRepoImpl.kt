package com.nullpointer.noursecompose.domain.alarms

import android.content.Context
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.room.daos.RegistryDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.registry.Registry
import com.nullpointer.noursecompose.models.registry.TypeRegistry
import com.nullpointer.noursecompose.services.MyAlarmManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(DelicateCoroutinesApi::class)
class AlarmRepoImpl(
    private val alarmDAO: AlarmDAO,
    private val registryDAO: RegistryDAO,
) : AlarmRepository {
    override suspend fun addNewRegistry(newRegistry: Registry) =
        registryDAO.insertRegistry(newRegistry)

    override suspend fun getAlarmById(id: Long): Alarm? =
        alarmDAO.getAlarmById(id)


    override suspend fun insertAlarm(alarm: Alarm, context: Context, isUpdate: Boolean): Long {
        val idAlarm = alarmDAO.insert(alarm)
        val registry = if (isUpdate) Registry(TypeRegistry.UPDATE, idAlarm)
        else Registry(TypeRegistry.CREATE, idAlarm)
        registryDAO.insertRegistry(registry)
        // ! is very important use this idAlarm
        MyAlarmManager.setAlarm(context, idAlarm, alarm.nextAlarm!!)
        return idAlarm
    }

    override suspend fun updateAlarm(alarm: Alarm, context: Context) {
        alarmDAO.updateAlarm(alarm)
        if (alarm.isActive) {
            MyAlarmManager.setAlarm(context, alarm.id!!, alarm.nextAlarm!!)
            registryDAO.insertRegistry(Registry(TypeRegistry.UPDATE, alarm.id))
        } else {
            MyAlarmManager.cancelAlarm(context, alarm.id!!)
            registryDAO.insertRegistry(Registry(TypeRegistry.UNREGISTER, alarm.id))
        }
    }

    override suspend fun deleterAlarm(alarm: Alarm, context: Context) {
        alarmDAO.deleterAlarm(alarm)
        alarm.nameFile?.let { ImageUtils.deleterImgFromStorage(it, context) }
        registryDAO.insertRegistry(Registry(TypeRegistry.DELETER, alarm.id!!))
        if (alarm.isActive) MyAlarmManager.cancelAlarm(context, alarm.id!!)
    }


    override suspend fun restoreAlarm(alarm: Alarm, context: Context) {
        MyAlarmManager.setAlarm(context, alarm.id!!, alarm.nextAlarm!!)
        registryDAO.insertRegistry(Registry(type = TypeRegistry.RESTORE, idAlarm = alarm.id!!))
    }

    override suspend fun deleterListAlarm(list: List<Long>, context: Context) {
        list.mapNotNull { alarmDAO.getAlarmById(it) }.forEach { alarm ->
            if (alarm.isActive) MyAlarmManager.cancelAlarm(context, alarm.id!!)
            registryDAO.insertRegistry(Registry(TypeRegistry.DELETER, alarm.id!!))
            alarm.nameFile?.let { ImageUtils.deleterImgFromStorage(it, context) }
        }
        alarmDAO.deleterListAlarm(list)
    }

    override fun getAllAlarmActive(): List<Alarm> =
        alarmDAO.getAllAlarmActive()

    override fun getAllAlarms(): Flow<List<Alarm>> =
        alarmDAO.getAllAlarms()

    override suspend fun getAlarmsForIds(listIds: List<Long>): List<Alarm> =
        alarmDAO.getListAlarmForIds(listIds)
}