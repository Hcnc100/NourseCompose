package com.nullpointer.noursecompose.domain.alarms

import android.content.Context
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.data.local.datasource.alarm.AlarmDataSource
import com.nullpointer.noursecompose.data.local.datasource.logs.LogsDataSource
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.registry.Log
import com.nullpointer.noursecompose.models.registry.TypeRegistry
import com.nullpointer.noursecompose.services.MyAlarmManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(DelicateCoroutinesApi::class)
class AlarmRepoImpl(
    private val alarmDataSource: AlarmDataSource,
    private val logsDataSource: LogsDataSource,
) : AlarmRepository {
    override val listAlarms: Flow<List<Alarm>> = alarmDataSource.listAlarms

    override suspend fun addNewLog(log: Log) =
        logsDataSource.insertLog(log)

    override suspend fun getAlarmById(id: Long): Alarm? =
        alarmDataSource.getAlarmById(id)


    override suspend fun insertAlarm(alarm: Alarm, context: Context, isUpdate: Boolean): Long {
        val idAlarm = alarmDataSource.insert(alarm)
        val log = if (isUpdate) Log(TypeRegistry.UPDATE, idAlarm) else Log(TypeRegistry.CREATE, idAlarm)
        logsDataSource.insertLog(log)
        // ! is very important use this idAlarm
        MyAlarmManager.setAlarm(context, idAlarm, alarm.nextAlarm!!)
        return idAlarm
    }

    override suspend fun updateAlarm(alarm: Alarm, context: Context) {
        alarmDataSource.updateAlarm(alarm)
        if (alarm.isActive) {
            MyAlarmManager.setAlarm(context, alarm.id!!, alarm.nextAlarm!!)
            logsDataSource.insertLog(Log(TypeRegistry.UPDATE, alarm.id))
        } else {
            MyAlarmManager.cancelAlarm(context, alarm.id!!)
            logsDataSource.insertLog(Log(TypeRegistry.UNREGISTER, alarm.id))
        }
    }

    override suspend fun deleterAlarm(alarm: Alarm, context: Context) {
        alarmDataSource.deleterAlarm(alarm)
        alarm.nameFile?.let { ImageUtils.deleterImgFromStorage(it, context) }
        logsDataSource.insertLog(Log(TypeRegistry.DELETER, alarm.id!!))
        if (alarm.isActive) MyAlarmManager.cancelAlarm(context, alarm.id)
    }


    override suspend fun restoreAlarm(alarm: Alarm, context: Context) {
        MyAlarmManager.setAlarm(context, alarm.id!!, alarm.nextAlarm!!)
        logsDataSource.insertLog(Log(type = TypeRegistry.RESTORE, idAlarm = alarm.id))
    }

    override suspend fun deleterListAlarm(list: List<Long>, context: Context) {
        list.mapNotNull { alarmDataSource.getAlarmById(it) }.forEach { alarm ->
            if (alarm.isActive) MyAlarmManager.cancelAlarm(context, alarm.id!!)
            logsDataSource.insertLog(Log(TypeRegistry.DELETER, alarm.id!!))
            alarm.nameFile?.let { ImageUtils.deleterImgFromStorage(it, context) }
        }
        alarmDataSource.deleterListAlarm(list)
    }


    override fun getAllAlarmActive(): List<Alarm> =
        alarmDataSource.getAllAlarmActive()


    override suspend fun getAlarmsForIds(listIds: List<Long>): List<Alarm> =
        alarmDataSource.getListAlarmForIds(listIds)
}