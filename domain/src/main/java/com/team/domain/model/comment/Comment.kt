package com.team.domain.model.comment

data class Comment(
    val commentId: Long,
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
    val content: String,
    val commentDate: String,
)
