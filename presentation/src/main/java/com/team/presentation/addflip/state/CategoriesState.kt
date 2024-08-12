package com.team.presentation.addflip.state

import com.team.domain.model.category.Category
import com.team.presentation.util.uitext.UiText

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val error: UiText = UiText.DynamicString("")
)