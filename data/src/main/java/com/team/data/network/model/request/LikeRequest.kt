package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeRequest(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "post_id") val postId: Int,
)