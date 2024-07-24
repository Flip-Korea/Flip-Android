package com.team.domain

import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.Flow

/**
 * DataStore Manager 클래스
 *
 * @see DataStoreType DataStore 키 값 집합
 */
interface DataStoreManager {

    /**
     * DataStore 에서 키 값을 통해 데이터를 가져온다. (String 타입)
     *
     * @param type DataStore 키 타입
     */
    fun getStringData(type: DataStoreType): Flow<String?>

    /**
     * DataStore 에서 키 값을 통해 데이터를 가져온다. (Int 타입)
     *
     * @param type DataStore 키 타입
     */
    fun getIntData(type: DataStoreType): Flow<Int?>

    /**
     * DataStore 에서 키 값을 통해 데이터를 저장한다. (String 타입)
     *
     * @param type DataStore 키 타입
     * @param data 데이터
     * @exception ClassCastException 타입에 맞지 않는 값을 넣을 때 해당 예외 발생
     */
    suspend fun <T : DataStoreType.TokenType> saveData(type: T, data: String)

    /**
     * DataStore 에서 키 값을 통해 데이터를 저장한다. (String 타입)
     *
     * @param type DataStore 키 타입
     * @param data 데이터
     * @exception ClassCastException 타입에 맞지 않는 값을 넣을 때 해당 예외 발생
     */
    suspend fun <T : DataStoreType.AccountType> saveData(type: T, data: String)

    /**
     * DataStore 에서 키 값을 통해 데이터를 저장한다. (Int 타입)
     *
     * @param type DataStore 키 타입
     * @param data 데이터
     * @exception ClassCastException 타입에 맞지 않는 값을 넣을 때 해당 예외 발생
     */
    suspend fun <T : DataStoreType.CheckType> saveData(type: T, data: Int)

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