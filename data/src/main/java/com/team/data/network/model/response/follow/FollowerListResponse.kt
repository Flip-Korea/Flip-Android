package com.team.data.network.model.response.follow

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.network.model.response.profile.DisplayProfileResponse
import com.team.data.network.model.response.profile.toDomainModel
import com.team.domain.model.profile.DisplayProfileList

@JsonClass(generateAdapter = true)
data class FollowerListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "followers") val followers: List<DisplayProfileResponse>
)

fun FollowerListResponse.toDomainModel(): DisplayProfileList =
    DisplayProfileList(hasNext, nextCursor, followers.toDomainModel())