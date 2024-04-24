package com.team.data.network.model.response.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.local.entity.profile.MyProfileEntity

/** 본인 프로필 (Res-2) **/
@JsonClass(generateAdapter = true)
data class MyProfileResponse(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "introduce") val introduce: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "post_cnt") val postCnt: Int,
    @Json(name = "follower_cnt") val followerCnt: Int,
    @Json(name = "following_cnt") val followingCnt: Int,
    @Json(name = "categories") val categories: List<Int>,
    @Json(name = "rating") val rating: String,
)

fun List<MyProfileResponse>.toEntity(): List<MyProfileEntity> = this.map { it.toEntity() }

fun MyProfileResponse.toEntity(): MyProfileEntity =
    MyProfileEntity(
        profileId = profileId,
        nickname = nickname,
        introduce = introduce,
        photoUrl = photoUrl,
        postCnt = postCnt,
        followerCnt = followerCnt,
        followingCnt = followingCnt,
        categories = categories,
        rating = rating,
    )