package com.dinoraw.chromelogs.domain.repository

import com.dinoraw.chromelogs.domain.model.Config
import kotlinx.coroutines.flow.Flow

interface ClientConfigRepository {
    suspend fun setConfig(config: Config): Result<Unit>
    fun getConfig(): Flow<Result<Config>>
}