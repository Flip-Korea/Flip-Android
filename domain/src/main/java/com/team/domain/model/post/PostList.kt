package com.team.domain.model.post

data class PostList(
    val hasNext: Boolean,
    val nextCursor: String,
    val postCnt: Long,
    val posts: List<Post>
)
