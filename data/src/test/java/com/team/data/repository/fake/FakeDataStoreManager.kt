package com.team.data.repository.fake

import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDataStoreManager: DataStoreManager {

    private val maps = mutableMapOf<String, String?>()
    private val fakeDelay = 500L

    override fun getData(type: DataStoreType): Flow<String?> = flow {
        delay(fakeDelay)
        val value = maps[getKey(type)]
        emit(value)
    }

    override suspend fun saveData(type: DataStoreType, data: String) {
        delay(fakeDelay)
        val key = getKey(type)
        maps[key] = data
    }

    override suspend fun deleteData(type: DataStoreType) {
        delay(fakeDelay)
        val key = getKey(type)
        maps.remove(key)
    }

    private fun getKey(type: DataStoreType): String =
        when (type) {
            DataStoreType.AccountType.CURRENT_PROFILE_ID -> "current_profile_id"
            DataStoreType.TokenType.ACCESS_TOKEN -> "access_token"
            DataStoreType.TokenType.REFRESH_TOKEN -> "refresh_token"
        }
}