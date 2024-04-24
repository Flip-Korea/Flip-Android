package com.team.data.network.model.response.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.comment.Comment

@JsonClass(generateAdapter = true)
data class CommentResponse(
    @Json(name = "comment_id") val commentId: Long,
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "photo_url") val photoUrl: String,
    @Json(name = "content") val content: String,
    @Json(name = "comment_date") val commentDate: String,
)

fun List<CommentResponse>.toExternal(): List<Comment> = this.map { it.toExternal() }

fun CommentResponse.toExternal(): Comment =
    Comment(commentId, profileId, nickname, photoUrl, content, commentDate)