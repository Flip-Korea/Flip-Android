package com.team.presentation.addflip.state

import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType

/**
 * 새로 작성되는 플립(Post) 폼 상태 모델
 */
data class NewPostState(
    val category: Category? = null,
    val title: String = "",
    val contents: List<String> = listOf(""),
    val bgColorType: BackgroundColorType = BackgroundColorType.DEFAULT,
    val tags: List<String> = emptyList()
)