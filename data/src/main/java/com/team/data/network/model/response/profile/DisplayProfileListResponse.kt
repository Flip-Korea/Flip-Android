package com.team.data.network.model.response.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.network.model.response.post.PostResponse

@JsonClass(generateAdapter = true)
data class DisplayProfileListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "profile_list") val profiles: List<DisplayProfileResponse>,
)
