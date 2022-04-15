package com.nullpointer.noursecompose.data.local.daos

import androidx.room.*
import com.nullpointer.noursecompose.models.registry.Registry
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistry(registry: Registry)

    @Delete
    suspend fun deleterRegistry(registry: Registry)

    @Query("DELETE FROM registry_table")
    suspend fun deleterAllRegistry()

    @Query("SELECT * FROM registry_table ORDER BY timestamp")
    fun getAllRegistry(): Flow<List<Registry>>
}