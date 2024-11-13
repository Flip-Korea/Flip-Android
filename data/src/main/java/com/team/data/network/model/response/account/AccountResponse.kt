package com.team.data.network.model.response.account

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.network.model.response.profile.MyProfileResponse
import com.team.domain.model.account.Account
import com.team.domain.model.profile.MyProfile

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

fun AccountResponse.toDomainModel(profiles: List<MyProfile>): Account =
    Account(
        email = email,
        name = name,
        phoneNum = phoneNum,
        profiles = profiles
    )