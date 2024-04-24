package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.comment.NewComment

@JsonClass(generateAdapter = true)
data class CommentRequest(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "post_id") val postId: Long,
    @Json(name = "comment") val comment: String,
)

fun NewComment.toNetwork(): CommentRequest =
    CommentRequest(profileId, postId, comment)