package com.team.data.network.model.response.post

import com.squareup.moshi.JsonClass
import com.team.domain.model.post.TempPostList
import com.team.domain.util.paging.FlipPagingData

@JsonClass(generateAdapter = true)
data class TempPostListResponse(
    val tempPosts: List<TempPostResponse>,
    val totalCount: Int
) : FlipPagingData<TempPostResponse> {
    override val list: List<TempPostResponse>
        get() = tempPosts
    override val firstKey: Long?
        get() = if (list.first().tempPostId == 0L) null else list.first().tempPostId
    override val lastKey: Long
        get() = list.last().tempPostId
}

fun TempPostListResponse.toDomainModel(): TempPostList =
    TempPostList(tempPosts.toDomainModel(), totalCount)