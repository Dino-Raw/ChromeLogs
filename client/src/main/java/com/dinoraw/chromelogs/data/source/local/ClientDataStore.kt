package com.dinoraw.chromelogs.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dinoraw.chromelogs.di.ClientModule
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.util.Const.DATA_STORE_CLIENT_CONFIG_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ClientDataStore @Inject constructor (
    @ClientModule private val dataStore: DataStore<Preferences>,
    private val defaultConfig: Config,
) {
    private val clientConfigKey = stringPreferencesKey(DATA_STORE_CLIENT_CONFIG_KEY)

    suspend fun write(config: Config): Result<Unit> =
       try {
           dataStore.edit { preferences ->
               preferences[clientConfigKey] = Json.encodeToString(config)
           }
           Result.success(Unit)
       } catch (e: Exception) {
           Result.failure(e)
       }

    fun read(): Flow<Result<Config>> = dataStore.data.map { preferences ->
        runCatching {
            val configString = preferences[clientConfigKey]
            if (configString != null) {
                Json.decodeFromString<Config>(configString)
            } else {
                defaultConfig
            }
        }
    }
}