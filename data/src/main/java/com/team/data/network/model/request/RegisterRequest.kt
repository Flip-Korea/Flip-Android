package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "account_id") val accountId: String,
    @Json(name = "name") val name: String,
    @Json(name = "profile") val profile: ProfileRequest,
)

@JsonClass(generateAdapter = true)
data class ProfileRequest(
    @Json(name = "nickname") val nickname: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "profile_id") val profileId: String,
)

fun RegisterProfile.toNetwork(): ProfileRequest =
    ProfileRequest(profileId = profileId, nickname = nickname, photoUrl = photoUrl)

fun Register.toNetwork(): RegisterRequest =
    RegisterRequest(
        accountId = accountId,
        name = accountName,
        profile = profile.toNetwork(),
    )
