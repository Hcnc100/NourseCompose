package com.nullpointer.noursecompose.domain.registry

import com.nullpointer.noursecompose.data.local.datasource.logs.LogsDataSource
import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

class RegistryRepoImpl(
    private val logsDataSource: LogsDataSource
) : RegistryRepository {
    override val listLogs: Flow<List<Log>> =
        logsDataSource.listLogs

    override suspend fun removeLog(log: Log) =
        logsDataSource.deleterLog(log)

    override suspend fun deleterAllLogs() =
        logsDataSource.deleterAllLog()

}