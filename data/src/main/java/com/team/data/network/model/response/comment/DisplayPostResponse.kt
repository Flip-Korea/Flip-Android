package com.team.data.network.model.response.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.post.DisplayPost

@JsonClass(generateAdapter = true)
data class DisplayPostResponse(
    @Json(name = "post_id") val postId: Long,
    @Json(name = "title") val title: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "my_comment") val myComment: String,
    @Json(name = "created_at") val createdAt: String,
)

fun DisplayPostResponse.toDomainModel(): DisplayPost =
    DisplayPost(postId, title, nickname, myComment, createdAt)

fun List<DisplayPostResponse>.toDomainModel(): List<DisplayPost> =
    this.map { it.toDomainModel() }