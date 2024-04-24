package com.team.data.datastore.fake

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FakeDataStoreManager {

    private val keyMap = mutableMapOf<String, String>()

    object TokenType {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    object AccountType {
        const val CURRENT_PROFILE_ID = "current_profile_id"
    }

    fun getToken(keyType: String): Flow<String?> {
        return flow {
            delay(1000L)
            emit(keyMap[keyType])
        }.catch { emit(null) }
    }

    suspend fun saveToken(keyType: String, value: String) {
        delay(1000L)
        keyMap[keyType] = value
    }

    suspend fun deleteToken(keyType: String) {
        delay(1000L)
        keyMap.remove(keyType)
    }

    fun clearAll() {
        keyMap.clear()
    }
}