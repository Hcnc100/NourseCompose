package com.nullpointer.noursecompose.data.local.datasource.logs

import com.nullpointer.noursecompose.data.local.room.daos.LogsDAO
import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

class LogsDataSourceImpl(
    private val logsDAO: LogsDAO,
) : LogsDataSource {
    override val listLogs: Flow<List<Log>> = logsDAO.getAllLogs()

    override suspend fun insertLog(newLog: Log) =
        logsDAO.insertRegistry(newLog)

    override suspend fun deleterLog(log: Log) =
        logsDAO.deleterLog(log)

    override suspend fun deleterAllLog() =
        logsDAO.deleterAllLogs()

    override suspend fun deleteLogs(listIds: List<Long>) =
        logsDAO.deleterLogs(listIds)

}