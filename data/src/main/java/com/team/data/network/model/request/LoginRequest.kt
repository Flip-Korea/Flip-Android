package com.team.data.network.model.request

import com.squareup.moshi.JsonClass
import com.team.domain.model.account.Login
import com.team.domain.type.SocialLoginPlatform

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val provider: SocialLoginPlatform,
    val oauthId: String,
)

fun Login.toNetwork(): LoginRequest = LoginRequest(provider, oauthId)
