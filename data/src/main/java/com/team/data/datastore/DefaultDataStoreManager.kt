package com.team.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// TODO 나중에 Encrypt/Decrypt 추가하기, 좀 더 자세히 작성할 필요 있음 (now in android 참고)
class DefaultDataStoreManager(private val context: Context): DataStoreManager {

    override fun getData(type: DataStoreType): Flow<String?> {
        val key = getKey(type)
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
            preferences[key]
        }
    }

    override suspend fun saveData(type: DataStoreType, data: String) {
        val key = getKey(type)
        context.dataStore.edit { preferences ->
            preferences[key] = data
        }
    }

    override suspend fun deleteData(type: DataStoreType) {
        val key = getKey(type)
        context.dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    private fun getKey(type: DataStoreType): Preferences.Key<String> =
        when (type) {
            DataStoreType.AccountType.CURRENT_PROFILE_ID -> stringPreferencesKey("current_profile_id")
            DataStoreType.TokenType.ACCESS_TOKEN -> stringPreferencesKey("access_token")
            DataStoreType.TokenType.REFRESH_TOKEN -> stringPreferencesKey("refresh_token")
        }
}