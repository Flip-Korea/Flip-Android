package com.team.data.network.model.request

import com.squareup.moshi.JsonClass
import com.team.domain.model.account.Login

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val provider: String,
    val oauthId: String,
)

fun Login.toNetwork(): LoginRequest = LoginRequest(provider.providerName, oauthId)
