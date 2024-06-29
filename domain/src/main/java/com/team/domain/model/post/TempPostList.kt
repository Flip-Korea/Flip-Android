package com.team.domain.model.post

data class TempPostList(
    val hasNext: Boolean,
    val nextCursor: String,
    val tempPostCnt: Int,
    val tempPosts: List<TempPost>
)
