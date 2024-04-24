package com.team.domain.model.post

data class TempPost(
    val profileId: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val categoryId: Int,
    val bgColorId: Int,
    val fontStyleId: Int,
    val tags: List<String>,
)
