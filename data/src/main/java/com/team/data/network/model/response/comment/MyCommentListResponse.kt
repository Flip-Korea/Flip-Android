package com.team.data.network.model.response.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.post.DisplayPostList

@JsonClass(generateAdapter = true)
data class MyCommentListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "posts") val posts: List<DisplayPostResponse>
)

fun MyCommentListResponse.toDomainModel(): DisplayPostList =
    DisplayPostList(hasNext, nextCursor, posts.toDomainModel())