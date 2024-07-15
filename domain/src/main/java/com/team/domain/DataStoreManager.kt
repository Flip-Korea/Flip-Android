package com.team.domain

import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.Flow

/**
 * DataStore Manager 클래스
 *
 * @see DataStoreType DataStore 키 값
 */
interface DataStoreManager {

    /**
     * DataStore 에서 키 값을 통해 데이터를 가져온다.
     *
     * @param type DataStore 키 타입
     */
    fun getData(type: DataStoreType): Flow<String?>

    /**
     * DataStore 에서 키 값을 통해 데이터를 저장한다.
     *
     * @param type DataStore 키 타입
     */
    suspend fun saveData(type: DataStoreType, data: String)

    /**
     * DataStore 에서 키 값을 통해 데이터를 삭제한다.
     *
     * @param type DataStore 키 타입
     */
    suspend fun deleteData(type: DataStoreType)

    /**
     * DataStore 데이터를 전부 삭제한다.
     */
    suspend fun clearAll()
}