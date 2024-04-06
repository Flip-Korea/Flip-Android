package com.team.data.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/** 다른 사용자의 프로필 (Res-1) **/
@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "introduce") val introduce: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "post_total") val postTotal: Int,
    @Json(name = "follower_total") val followerTotal: Int,
    @Json(name = "following_total") val followingTotal: Int,
    @Json(name = "is_follower") val isFollower: Boolean,
    @Json(name = "is_following") val isFollowing: Boolean,
    @Json(name = "rating") val rating: String
)