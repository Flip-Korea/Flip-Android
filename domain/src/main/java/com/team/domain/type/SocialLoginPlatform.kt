package com.team.domain.type

enum class SocialLoginPlatform {
    GOOGLE, KAKAO
}

fun SocialLoginPlatform.asString(): String {
    return when (this) {
        SocialLoginPlatform.GOOGLE -> { "google" }
        SocialLoginPlatform.KAKAO -> { "kakao" }
    }
}