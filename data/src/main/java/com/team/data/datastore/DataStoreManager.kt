package com.team.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// TODO 나중에 Encrypt/Decrypt 추가하기, 좀 더 자세히 작성할 필요 있음 (now in android 참고)
class DataStoreManager(private val context: Context) {
    object TokenType {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    object AccountType {
        val CURRENT_PROFILE_ID = stringPreferencesKey("current_profile_id")
    }

    fun getToken(type: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
            preferences[type]
        }
    }

    suspend fun saveToken(type: Preferences.Key<String>, token: String) {
        context.dataStore.edit { preferences ->
            preferences[type] = token
        }
    }

    suspend fun deleteToken(type: Preferences.Key<String>) {
        context.dataStore.edit { preferences ->
            preferences.remove(type)
        }
    }
}