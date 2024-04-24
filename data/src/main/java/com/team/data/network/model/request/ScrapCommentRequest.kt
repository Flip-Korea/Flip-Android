package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScrapCommentRequest(
    @Json(name = "scrap_comment")
    val scrapComment: String
)
