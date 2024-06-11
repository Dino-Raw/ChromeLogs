package com.dinoraw.chromelogs.di

import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.model.GestureServiceResult
import com.dinoraw.chromelogs.domain.model.Message
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientDomainModule {
    @Provides
    @Singleton
    fun provideConfig(): Config = Config("127.0.0.1", 8080)

    @Provides
    @Singleton
    fun provideMessage(): Message = Message("", "")

    @Provides
    @Singleton
    fun provideGestureServiceResult() = GestureServiceResult(0, 0, 0 ,0, 0, 0)
}