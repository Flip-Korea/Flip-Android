package com.team.domain.model.account

import com.team.domain.type.SocialLoginPlatform

data class Login(
    val provider: SocialLoginPlatform,
    val oauthId: String,
)
