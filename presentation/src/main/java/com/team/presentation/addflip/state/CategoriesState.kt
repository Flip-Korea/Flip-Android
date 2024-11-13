package com.team.presentation.addflip.state

import com.team.domain.model.category.Category
import com.team.presentation.util.uitext.UiText

/**
 * 모든 카테고리를 불러오는 상태 모델
 */
data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val error: UiText = UiText.DynamicString("")
)