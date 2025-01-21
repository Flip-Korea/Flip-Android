package com.team.domain.type

enum class SocialLoginPlatform(
    val providerName: String,
) {
    Google("google"),
    Kakao("kakao"),
}

fun String.toSocialLoginPlatform(): SocialLoginPlatform? =
    when (this) {
        SocialLoginPlatform.Google.providerName -> SocialLoginPlatform.Google
        SocialLoginPlatform.Kakao.providerName -> SocialLoginPlatform.Kakao
        else -> null
    }
