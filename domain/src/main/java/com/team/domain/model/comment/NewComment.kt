package com.team.domain.model.comment

data class NewComment(
    val profileId: String,
    val postId: Long,
    val comment: String
)
