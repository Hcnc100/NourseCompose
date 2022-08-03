package com.nullpointer.noursecompose.domain.registry

import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

interface RegistryRepository {
    val listLogs: Flow<List<Log>>
    suspend fun removeLog(log: Log)
    suspend fun deleterAllLogs()
    suspend fun deleterLogs(listId: List<Long>)
}