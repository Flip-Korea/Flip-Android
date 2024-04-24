package com.team.data.network.model.response.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.local.entity.profile.DisplayProfileEntity
import com.team.domain.model.profile.DisplayProfile

@JsonClass(generateAdapter = true)
data class DisplayProfileResponse(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "following") val following: Boolean,
    @Json(name = "follower_cnt") val followerCnt: Long?
)

fun DisplayProfileResponse.toEntity(): DisplayProfileEntity =
    DisplayProfileEntity(profileId, nickname, photoUrl, following, followerCnt)

fun DisplayProfileResponse.toExternal(): DisplayProfile =
    DisplayProfile(profileId, nickname, photoUrl, following, followerCnt)