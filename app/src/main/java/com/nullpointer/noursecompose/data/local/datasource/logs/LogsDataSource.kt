package com.nullpointer.noursecompose.data.local.datasource.logs

import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

interface LogsDataSource {
    val listLogs:Flow<List<Log>>
    suspend fun insertLog(newLog: Log)
    suspend fun deleterLog(log: Log)
    suspend fun deleterAllLog()
}