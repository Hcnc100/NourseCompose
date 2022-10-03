package com.nullpointer.noursecompose.domain.logger

import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

interface LogsRepository {
    val listLogs: Flow<List<Log>>
    suspend fun removeLog(log: Log)
    suspend fun deleterAllLogs()
    suspend fun deleterLogs(listId: List<Long>)
}