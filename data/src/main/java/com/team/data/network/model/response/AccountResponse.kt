package com.team.data.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/** 계정 (Res-1) **/
@JsonClass(generateAdapter = true)
data class AccountResponse(
    @Json(name = "account_id")
    val accountId: String,
    val email: String,
    val name: String,
    @Json(name = "phone_num")
    val phoneNum: String,
    val profile: List<MyProfileResponse>
)