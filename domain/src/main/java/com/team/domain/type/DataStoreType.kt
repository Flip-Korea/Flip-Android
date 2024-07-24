package com.team.domain.type

//TODO 추후에 자식 클래스들을 data class로 교체 하는게 나을 것이라고 예상
/**
 * DataStore 키 값 집합
 */
sealed interface DataStoreType {

    /**
     * TokenType
     * String 타입의 값만 허용
     */
    enum class TokenType: DataStoreType {
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    /**
     * AccountType
     * String 타입의 값만 허용
     */
    enum class AccountType: DataStoreType {
        CURRENT_PROFILE_ID
    }

    /**
     * CheckType
     * Int 타입의 값만 허용
     */
    enum class CheckType: DataStoreType {
        EDIT_MY_CATEGORIES_SPEECH_BUBBLE
    }
}