package com.team.data.network.model.response.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "post_cnt") val postCnt: Long,
    @Json(name = "posts") val posts: List<PostResponse>
)