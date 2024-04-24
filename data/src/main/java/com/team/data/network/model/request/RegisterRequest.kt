package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRegister(
//    val _email: String,
//    val _phone_num: String,
    @Json(name = "account_id")
    val accountId: String,
    @Json(name = "categories")
    val categories: List<Int>,
    @Json(name = "name")
    val name: String,
    @Json(name = "profile")
    val profile: NetworkProfile
)

@JsonClass(generateAdapter = true)
data class NetworkProfile(
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "photo_url")
    val photoUrl: String,
    @Json(name = "profile_id")
    val profileId: String
)