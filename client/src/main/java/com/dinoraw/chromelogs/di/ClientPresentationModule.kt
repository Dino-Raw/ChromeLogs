package com.dinoraw.chromelogs.di

import com.dinoraw.chromelogs.presentation.model.ClientUIState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientPresentationModule {
    @Provides
    @Singleton
    fun provideClientUIState(): ClientUIState = ClientUIState(
        buttonStartStopText = "Start",
        buttonStartStopEnabled = true,
        buttonStartStopOnClick = { },
        buttonConfigEnabled = true,
    )
}