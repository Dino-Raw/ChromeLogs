package com.dinoraw.chromelogs.di

import com.dinoraw.chromelogs.data.repository.ClientConfigRepositoryImpl
import com.dinoraw.chromelogs.data.repository.ClientWebSocketRepositoryImpl
import com.dinoraw.chromelogs.domain.repository.ClientConfigRepository
import com.dinoraw.chromelogs.domain.repository.ClientWebSocketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClientRepositoryModule {
    @Binds
    abstract fun provideClientConfigRepository(
        clientConfigRepositoryImpl: ClientConfigRepositoryImpl
    ): ClientConfigRepository

    @Binds
    abstract fun provideClientWebSocketRepository(
        clientWebSocketRepositoryImpl: ClientWebSocketRepositoryImpl
    ): ClientWebSocketRepository

}