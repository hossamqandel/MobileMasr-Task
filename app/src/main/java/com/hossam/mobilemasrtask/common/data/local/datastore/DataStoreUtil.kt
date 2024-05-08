package com.hossam.mobilemasrtask.common.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreUtil(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    companion object {
        private val KEY_TOKEN = stringPreferencesKey("user_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("user_refresh_token")
    }


    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearAll(): Boolean {
        saveToken("")
        saveRefreshToken("")
        return getTokenAsFlow.first().isNullOrEmpty() &&
                getRefreshTokenAsFlow.first().isNullOrEmpty()
    }

    val getTokenAsFlow: Flow<String?> = context
        .dataStore.data.map { preferences ->
        preferences[KEY_TOKEN]
    }

    val getRefreshTokenAsFlow: Flow<String?> = context
        .dataStore.data.map { preferences ->
            preferences[KEY_REFRESH_TOKEN]
        }


}