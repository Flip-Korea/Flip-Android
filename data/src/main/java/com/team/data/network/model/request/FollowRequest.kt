package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FollowRequest(
    @Json(name = "following_id") val followingId: String,
    @Json(name = "follower_id") val followerId: String
)