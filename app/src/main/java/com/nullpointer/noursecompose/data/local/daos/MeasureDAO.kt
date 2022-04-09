package com.nullpointer.noursecompose.data.local.daos

import androidx.room.*
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measure: SimpleMeasure): Long

    @Delete
    suspend fun deleterMeasure(measure: SimpleMeasure)

    @Query("DELETE FROM measure_table WHERE id IN (:listIdDeleter)")
    suspend fun deleterListMeasure(listIdDeleter: List<Long>)

    @Query("SELECT * FROM measure_table WHERE typeMeasure = :measureType ORDER BY timestamp DESC")
    fun getAllMeasureForType(measureType: MeasureType): Flow<List<SimpleMeasure>>
}