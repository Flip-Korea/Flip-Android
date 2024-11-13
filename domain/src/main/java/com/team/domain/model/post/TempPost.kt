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
) {
    constructor(
        tempPostId: Long,
        title: String,
        content: String,
        bgColorType: BackgroundColorType,
        categoryId: Int,
        categoryName: String,
        postAt: String
    ): this(
        tempPostId,
        title,
        content,
        bgColorType,
        FontStyleType.NORMAL,
        categoryId,
        categoryName,
        emptyList(),
        postAt
    )
}
