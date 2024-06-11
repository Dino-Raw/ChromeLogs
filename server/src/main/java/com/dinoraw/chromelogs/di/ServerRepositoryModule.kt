package com.dinoraw.chromelogs.di

import com.dinoraw.chromelogs.data.repository.ServerConfigRepositoryImpl
import com.dinoraw.chromelogs.data.repository.ServerGestureRepositoryImpl
import com.dinoraw.chromelogs.domain.repository.ServerConfigRepository
import com.dinoraw.chromelogs.domain.repository.ServerGestureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServerRepositoryModule {
    @Binds
    abstract fun provideServerConfigRepository(
        serverConfigRepositoryImpl: ServerConfigRepositoryImpl
    ): ServerConfigRepository

    @Binds
    abstract fun provideServerGestureRepository(
        serverGestureRepositoryImpl: ServerGestureRepositoryImpl
    ): ServerGestureRepository
}