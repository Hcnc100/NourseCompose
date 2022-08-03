package com.nullpointer.noursecompose.data.local.room.daos

import androidx.room.*
import com.nullpointer.noursecompose.models.registry.Log
import kotlinx.coroutines.flow.Flow

@Dao
interface LogsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistry(newLog: Log)

    @Delete
    suspend fun deleterLog(log: Log)

    @Query("DELETE FROM logs_table")
    suspend fun deleterAllLogs()

    @Query("SELECT * FROM logs_table ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<Log>>

    @Query("DELETE FROM logs_table where id in (:listIds)")
    suspend fun deleterLogs(listIds:List<Long>)
}