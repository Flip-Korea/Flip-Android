package com.team.data.network.model.request

import com.squareup.moshi.JsonClass
import com.team.domain.model.post.NewPost

@JsonClass(generateAdapter = true)
data class PostRequest(
    val title: String,
    val content: String,
    val bgColorType: String,
    val fontStyleType: String,
    val tags: List<String>,
    val categoryId: Int,
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

