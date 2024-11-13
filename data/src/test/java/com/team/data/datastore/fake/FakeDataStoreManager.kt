package com.team.data.datastore.fake

import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDataStoreManager: DataStoreManager {

    private val maps = mutableMapOf<String, String?>()
    private val intMaps = mutableMapOf<String, Int?>()
    private val booleanMaps = mutableMapOf<String, Boolean?>()
    private val fakeDelay = 500L

    override fun getStringData(type: DataStoreType): Flow<String?> = flow {
        delay(fakeDelay)
        val value = maps[getKey(type)]
        emit(value)
    }

    override fun getIntData(type: DataStoreType): Flow<Int?> = flow {
        delay(fakeDelay)
        val value = intMaps[getKey(type)]
        emit(value)
    }

    override suspend fun <T : DataStoreType.TokenType> saveData(type: T, data: String) {
        delay(fakeDelay)
        val key = getKey(type)
        maps[key] = data
    }

    override suspend fun <T : DataStoreType.AccountType> saveData(type: T, data: String) {
        delay(fakeDelay)
        val key = getKey(type)
        maps[key] = data
    }

    override suspend fun <T : DataStoreType.CheckType> saveData(type: T, data: Int) {
        delay(fakeDelay)
        val key = getKey(type)
        intMaps[key] = data
    }

    override suspend fun deleteData(type: DataStoreType) {
        delay(fakeDelay)
        when (val key = getKey(type)) {
            in maps -> {
                maps.remove(key)
                return
            }
            in intMaps -> {
                intMaps.remove(key)
                return
            }
            in booleanMaps -> {
                booleanMaps.remove(key)
                return
            }
        }
    }

    override suspend fun clearAll() {
        maps.clear()
    }

    private fun getKey(type: DataStoreType): String =
        when (type) {
            DataStoreType.AccountType.CURRENT_PROFILE_ID -> "current_profile_id"
            DataStoreType.TokenType.ACCESS_TOKEN -> "access_token"
            DataStoreType.TokenType.REFRESH_TOKEN -> "refresh_token"
            DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE -> "edit_my_categories_speech_bubble"
        }
}