package com.team.presentation.home.state

import com.team.domain.model.category.Category
import com.team.presentation.home.util.HomeTabMockItems

data class CategoryState(
    val categories: List<Category> = HomeTabMockItems,
    val splitSize: Int = 0
)
