package com.team.domain.type

enum class LoginPlatformType {
    GOOGLE, APPLE, KAKAO
}

fun LoginPlatformType.asString(): String {
    return when (this) {
        LoginPlatformType.GOOGLE -> { "google" }
        LoginPlatformType.APPLE -> { "apple" }
        LoginPlatformType.KAKAO -> { "kakao" }
    }
}