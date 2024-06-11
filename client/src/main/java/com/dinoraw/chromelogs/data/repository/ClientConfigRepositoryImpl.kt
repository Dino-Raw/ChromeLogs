package com.dinoraw.chromelogs.data.repository

import com.dinoraw.chromelogs.data.source.local.ClientDataStore
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.repository.ClientConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientConfigRepositoryImpl @Inject constructor(
    private val clientDataStore: ClientDataStore
): ClientConfigRepository {
    override suspend fun setConfig(config: Config): Result<Unit> =
        clientDataStore.write(config = config)

    override fun getConfig(): Flow<Result<Config>> =
        clientDataStore.read()
}