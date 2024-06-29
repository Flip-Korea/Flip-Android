package com.team.domain.model.post

data class DisplayPostList(
    val hasNext: Boolean,
    val nextCursor: String,
    val displayPosts: List<DisplayPost>
)
