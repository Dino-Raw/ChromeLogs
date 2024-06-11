package com.dinoraw.chromelogs.data.source.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStoreServer by preferencesDataStore("DataStoreServer")