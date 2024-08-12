package com.team.presentation.editcategories.state

import com.team.domain.model.category.Category
import com.team.presentation.util.uitext.UiText

data class MyCategoriesState(
    val myCategories: List<Category> = emptyList(),
    val exclusiveCategories: List<Category> = emptyList(),
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
