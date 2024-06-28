package com.team.data.network.model.response.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.post.TempPost

@JsonClass(generateAdapter = true)
data class TempPostResponse(
    @Json(name = "profile_id") val profileId: String,
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "category_id") val categoryId: Int,
    @Json(name = "bg_color_id") val bgColorId: Int,
    @Json(name = "font_style_id") val fontStyleId: Int,
    @Json(name = "tags") val tags: List<String>,
)

fun List<TempPostResponse>.toDomainModel(): List<TempPost> =
    this.map { it.toDomainModel() }

fun TempPostResponse.toDomainModel(): TempPost =
    TempPost(profileId, title, content, createdAt, categoryId, bgColorId, fontStyleId, tags)