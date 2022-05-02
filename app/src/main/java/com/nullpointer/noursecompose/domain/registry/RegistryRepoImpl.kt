package com.nullpointer.noursecompose.domain.registry

import com.nullpointer.noursecompose.data.local.room.daos.RegistryDAO
import com.nullpointer.noursecompose.models.registry.Registry
import kotlinx.coroutines.flow.Flow

class RegistryRepoImpl(
    private val registryDAO: RegistryDAO,
) : RegistryRepository {
    override fun getAllRegistry(): Flow<List<Registry>> =
        registryDAO.getAllRegistry()

    override suspend fun removeRegistry(registry: Registry) =
        registryDAO.deleterRegistry(registry)

    override suspend fun deleterAllAlarm() =
        registryDAO.deleterAllRegistry()
}