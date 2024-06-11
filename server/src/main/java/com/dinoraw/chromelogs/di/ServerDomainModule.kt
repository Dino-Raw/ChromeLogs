package com.dinoraw.chromelogs.di

import com.dinoraw.chromelogs.domain.model.ClientGestureData
import com.dinoraw.chromelogs.domain.model.GestureServiceResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerDomainModule {

    @Singleton
    @Provides
    fun provideGestureServiceResult() = GestureServiceResult(0,0,0,0,0,0)

    @Singleton
    @Provides
    fun provideClientGestureData(gestureServiceResult: GestureServiceResult) = ClientGestureData(0, "", gestureServiceResult)
}