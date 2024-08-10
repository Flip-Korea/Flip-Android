package com.team.domain.model.post

data class NewPost(
    val title: String,
    val content: String,
    val bgColorType: String,
    val fontStyleType: String,
    val tags: List<String>,
    val categoryId: Int?,
)