package com.nullpointer.noursecompose.domain.registry

import com.nullpointer.noursecompose.models.registry.Registry
import kotlinx.coroutines.flow.Flow

interface RegistryRepository {
    suspend fun getAllRegistry(): Flow<List<Registry>>
    suspend fun removeRegistry(registry: Registry)
}