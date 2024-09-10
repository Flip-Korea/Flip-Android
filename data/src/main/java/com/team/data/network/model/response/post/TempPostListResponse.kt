package com.team.data.network.model.response.post

import com.squareup.moshi.JsonClass
import com.team.domain.model.post.TempPostList

@JsonClass(generateAdapter = true)
data class TempPostListResponse(
    val tempPosts: List<TempPostResponse>,
    val totalCount: Int
) {
    val cursor: Long
        get() = tempPosts.last().tempPostId
}

fun TempPostListResponse.toDomainModel(): TempPostList =
    TempPostList(tempPosts.toDomainModel(), totalCount)