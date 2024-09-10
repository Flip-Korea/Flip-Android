package com.team.domain.model.post

import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

data class NewPost(
    val title: String,
    val content: String,
    val bgColorType: BackgroundColorType,
    val fontStyleType: FontStyleType,
    val tags: List<String>,
    val categoryId: Int?,
)