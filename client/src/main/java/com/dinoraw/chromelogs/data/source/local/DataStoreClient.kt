package com.dinoraw.chromelogs.data.source.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStoreClient by preferencesDataStore("DataStoreClient")