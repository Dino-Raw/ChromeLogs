package com.dinoraw.chromelogs.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dinoraw.chromelogs.data.source.local.dataStoreClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class ClientModule

@Module
@InstallIn(SingletonComponent::class)
object ClientDataModule {
    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient = HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    @Provides
    @Singleton
    @ClientModule
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStoreClient
}