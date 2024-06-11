package com.dinoraw.chromelogs.di


import android.content.Context
import androidx.room.Room
import com.dinoraw.chromelogs.data.source.local.dataStoreServer
import com.dinoraw.chromelogs.data.source.local.database.GestureDao
import com.dinoraw.chromelogs.data.source.local.database.GestureDataBase
import com.dinoraw.chromelogs.domain.model.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.NetworkInterface
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class ServerModule

@Module
@InstallIn(SingletonComponent::class)
object ServerDataModule {
    @Provides
    @Singleton
    fun provideGestureDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        GestureDataBase::class.java,
        "GestureDataBase"
    ).build()

    @Singleton
    @Provides
    fun provideGestureDao(gestureDataBase: GestureDataBase): GestureDao = gestureDataBase.gestureDao()

    @Provides
    @Singleton
    fun provideConfig(): Config {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces().iterator().asSequence()
        val localAddresses = networkInterfaces.flatMap {
            it.inetAddresses.asSequence()
                .filter { inetAddress ->
                    inetAddress.isSiteLocalAddress &&
                            inetAddress.hostAddress?.contains(":") == false &&
                            inetAddress.hostAddress != "127.0.0.1"
                }.map { inetAddress -> inetAddress.hostAddress }
        }
        return Config(localAddresses.firstOrNull().toString(), 8080)
    }

    @Provides
    @Singleton
    @ServerModule
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStoreServer
}