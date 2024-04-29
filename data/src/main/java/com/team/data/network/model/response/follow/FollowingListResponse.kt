package com.team.data.network.model.response.follow

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.network.model.response.profile.DisplayProfileResponse

@JsonClass(generateAdapter = true)
data class FollowingListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "followings") val followings: List<DisplayProfileResponse>
)