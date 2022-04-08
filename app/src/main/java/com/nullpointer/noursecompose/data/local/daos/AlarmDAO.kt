package com.nullpointer.noursecompose.data.local.daos

import androidx.room.*
import com.nullpointer.noursecompose.models.alarm.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Alarm): Long

    @Delete
    suspend fun deleterAlarm(item: Alarm)

    @Update
    suspend fun updateAlarm(item: Alarm)

    @Query("DELETE FROM alarms_table WHERE id=:idDeleter")
    suspend fun deleterAlarm(idDeleter: Int)

    @Query("DELETE FROM alarms_table WHERE id IN (:listIdDeleter)")
    suspend fun deleterListAlarm(listIdDeleter: List<Long>)

    @Query("SELECT * FROM alarms_table ORDER BY createdAt DESC")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms_table WHERE id=:id")
    suspend fun getAlarmById(id: Long): Alarm?

    @Query("SELECT * FROM alarms_table WHERE isActive")
    suspend fun getAllAlarmActive(): List<Alarm>
}