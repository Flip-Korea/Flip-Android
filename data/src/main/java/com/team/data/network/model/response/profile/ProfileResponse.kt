package com.team.data.network.model.response.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.profile.MyProfile
import com.team.domain.model.profile.Profile

/** 다른 사용자의 프로필 (Res-1) **/
@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "introduce") val introduce: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "post_cnt") val postCnt: Int,
    @Json(name = "follower_cnt") val followerCnt: Int,
    @Json(name = "following_cnt") val followingCnt: Int,
    @Json(name = "is_follower") val isFollower: Boolean,
    @Json(name = "is_following") val isFollowing: Boolean,
    @Json(name = "rating") val rating: String
)

fun ProfileResponse.toExternal(): Profile =
    Profile(profileId, nickname, introduce, photoUrl, postCnt, followerCnt, followingCnt, isFollower, isFollowing, rating)