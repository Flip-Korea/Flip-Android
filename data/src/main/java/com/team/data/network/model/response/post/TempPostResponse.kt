package com.team.data.network.model.response.post

import com.squareup.moshi.JsonClass
import com.team.domain.model.post.TempPost
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

@JsonClass(generateAdapter = true)
data class TempPostResponse(
    val tempPostId: Long,
    val title: String,
    val content: String,
    val bgColorType: BackgroundColorType,
    val fontStyleType: FontStyleType,
    val categoryId: Int,
    val categoryName: String,
    val tags: List<String>,
    val postAt: String,
)

fun List<TempPostResponse>.toDomainModel(): List<TempPost> =
    this.map { it.toDomainModel() }

fun TempPostResponse.toDomainModel(): TempPost =
    TempPost(tempPostId, title, content, bgColorType, fontStyleType, categoryId, categoryName, tags, postAt)