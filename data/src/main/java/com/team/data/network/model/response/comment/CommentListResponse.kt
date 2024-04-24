package com.team.data.network.model.response.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.comment.CommentList

@JsonClass(generateAdapter = true)
data class CommentListResponse(
    @Json(name = "comment_cnt") val commentCnt: Int,
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "next_cursor") val nextCursor: String,
    @Json(name = "comment") val comments: List<CommentResponse>,
)

fun CommentListResponse.toExternal(): CommentList =
    CommentList(
        commentCnt = commentCnt,
        hasNext = hasNext,
        nextCursor = nextCursor,
        comments = comments.toExternal()
    )