package com.dinoraw.chromelogs.di

import com.dinoraw.chromelogs.presentation.model.ServerUIState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerPresentationModule {
    @Provides
    @Singleton
    fun provideServerUIState(): ServerUIState = ServerUIState(
        buttonStartStopText = "Start",
        buttonStartStopOnClick = { },
        buttonConfigEnabled = true,
    )
}