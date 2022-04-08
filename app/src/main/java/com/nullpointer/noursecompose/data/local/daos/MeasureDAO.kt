package com.nullpointer.noursecompose.data.local.daos

import androidx.room.*
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SimpleMeasure): Long

    @Delete
    suspend fun deleterOxygen(item: SimpleMeasure)

    @Query("DELETE FROM measure_table WHERE id IN (:listIdDeleter)")
    suspend fun deleterListOxygen(listIdDeleter: List<Long>)

    @Query("SELECT * FROM measure_table ORDER BY timestamp DESC")
    fun getAllOxygen(): Flow<List<SimpleMeasure>>
}