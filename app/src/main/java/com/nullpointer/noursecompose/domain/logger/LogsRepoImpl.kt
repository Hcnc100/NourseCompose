package com.nullpointer.noursecompose.domain.logger

import com.nullpointer.noursecompose.data.local.datasource.logs.LogsDataSource
import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

class LogsRepoImpl(
    private val logsDataSource: LogsDataSource
) : LogsRepository {
    override val listLogs: Flow<List<Log>> =
        logsDataSource.listLogs

    override suspend fun removeLog(log: Log) =
        logsDataSource.deleterLog(log)

    override suspend fun deleterAllLogs() =
        logsDataSource.deleterAllLog()

    override suspend fun deleterLogs(listId: List<Long>) =
        logsDataSource.deleteLogs(listId)


}