package com.team.domain.model.comment

data class CommentList(
    val commentCnt: Int,
    val hasNext: Boolean,
    val nextCursor: String,
    val comments: List<Comment>,
)
