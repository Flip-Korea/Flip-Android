package com.team.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private sealed class PreferenceKeyType {
    data class StringType(val key: Preferences.Key<String>): PreferenceKeyType()
    data class IntType(val key: Preferences.Key<Int>): PreferenceKeyType()
    data class BooleanType(val key: Preferences.Key<Boolean>): PreferenceKeyType()
}

class DefaultDataStoreManager(private val dataStore: DataStore<Preferences>) : DataStoreManager {
    override fun getStringData(type: DataStoreType): Flow<String?> {
        val key = (getKey(type) as PreferenceKeyType.StringType).key
        return dataStore.data
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

    override fun getIntData(type: DataStoreType): Flow<Int?> {
        val key = (getKey(type) as PreferenceKeyType.IntType).key
        return dataStore.data
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

    override suspend fun <T : DataStoreType.TokenType> saveData(type: T, data: String) {
        val key = (getKey(type) as PreferenceKeyType.StringType).key
        dataStore.edit { preferences ->
            preferences[key] = data
        }
    }

    override suspend fun <T : DataStoreType.AccountType> saveData(type: T, data: String) {
        val key = (getKey(type) as PreferenceKeyType.StringType).key
        dataStore.edit { preferences ->
            preferences[key] = data
        }
    }

    override suspend fun <T : DataStoreType.CheckType> saveData(type: T, data: Int) {
        val key = (getKey(type) as PreferenceKeyType.IntType).key
        dataStore.edit { preferences ->
            preferences[key] = data
        }
    }

    override suspend fun deleteData(type: DataStoreType) {
        val key = when(val k = getKey(type)) {
            is PreferenceKeyType.IntType -> k.key
            is PreferenceKeyType.StringType -> k.key
            is PreferenceKeyType.BooleanType -> k.key
        }
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    private fun getKey(type: DataStoreType): PreferenceKeyType =
        when (type) {
            DataStoreType.AccountType.CURRENT_PROFILE_ID -> {
                PreferenceKeyType.StringType(stringPreferencesKey("current_profile_id"))
            }
            DataStoreType.TokenType.ACCESS_TOKEN -> {
                PreferenceKeyType.StringType(stringPreferencesKey("access_token"))
            }
            DataStoreType.TokenType.REFRESH_TOKEN -> {
                PreferenceKeyType.StringType(stringPreferencesKey("refresh_token"))
            }
            DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE -> {
                PreferenceKeyType.IntType(intPreferencesKey("edit_my_categories_speech_bubble"))
            }
        }
}