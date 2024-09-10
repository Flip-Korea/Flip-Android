package com.team.domain.model.post

import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

data class TempPost(
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
