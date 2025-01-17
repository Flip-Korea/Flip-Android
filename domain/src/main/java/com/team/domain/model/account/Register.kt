package com.team.domain.model.account

import com.team.domain.type.SocialLoginPlatform

data class Register(
    val socialLoginPlatform: SocialLoginPlatform,
    val oauthId: String,
    val profile: RegisterProfile,
    val adsAgree: Boolean,
)

data class RegisterProfile(
    val userId: String,
    val nickname: String,
    val photoUrl: String,
)
