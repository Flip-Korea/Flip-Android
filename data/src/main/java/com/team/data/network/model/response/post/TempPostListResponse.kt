package com.team.data.network.model.response.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.post.TempPostList

@JsonClass(generateAdapter = true)
data class TempPostListResponse(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "temp_post_cnt") val tempPostCnt: Int,
    @Json(name = "temp_posts") val tempPosts: List<TempPostResponse>
)

fun TempPostListResponse.toDomainModel(): TempPostList =
    TempPostList(hasNext, nextCursor, tempPostCnt, tempPosts.toDomainModel())