package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.scrap.NewScrap

@JsonClass(generateAdapter = true)
data class ScrapRequest(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "post_id") val postId: Int,
    @Json(name = "message") val message: String?,
)

fun NewScrap.toNetwork(): ScrapRequest =
    ScrapRequest(profileId, postId, message)