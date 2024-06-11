package com.dinoraw.chromelogs.data.repository

import com.dinoraw.chromelogs.data.source.local.ServerDataStore
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.repository.ServerConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ServerConfigRepositoryImpl @Inject constructor(
    private val serverDataStore: ServerDataStore
): ServerConfigRepository {
    override suspend fun setConfig(config: Config): Result<Unit> =
        serverDataStore.write(config = config)

    override fun getConfig(): Flow<Result<Config>> =
        serverDataStore.read()
}