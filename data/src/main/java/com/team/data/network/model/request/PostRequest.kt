package com.team.data.network.model.request

import com.squareup.moshi.JsonClass
import com.team.domain.model.post.NewPost
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

@JsonClass(generateAdapter = true)
data class PostRequest(
    val title: String,
    val content: String,
    val bgColorType: BackgroundColorType,
    val fontStyleType: FontStyleType,
    val tags: List<String>,
    val categoryId: Int?,
)

fun NewPost.toNetwork(): PostRequest =
    PostRequest(
        title = title,
        content = content,
        categoryId = categoryId,
        bgColorType = bgColorType,
        fontStyleType = fontStyleType,
        tags = tags,
    )

