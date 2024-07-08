package com.team.domain.type

sealed interface DataStoreType {

    enum class TokenType: DataStoreType {
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    enum class AccountType: DataStoreType {
        CURRENT_PROFILE_ID
    }
}