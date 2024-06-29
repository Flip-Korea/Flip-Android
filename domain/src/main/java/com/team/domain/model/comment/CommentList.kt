package com.team.domain.model.comment

data class CommentList(
    val hasNext: Boolean,
    val nextCursor: String,
    val commentCnt: Int,
    val comments: List<Comment>,
)
