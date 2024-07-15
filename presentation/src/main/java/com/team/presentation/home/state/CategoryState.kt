package com.team.presentation.home.state

import com.team.domain.model.category.Category

data class CategoryState(
    val categories: List<Category> = emptyList(),
    val splitSize: Int = 3
)
