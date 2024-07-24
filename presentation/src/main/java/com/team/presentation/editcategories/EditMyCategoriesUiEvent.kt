package com.team.presentation.editcategories

import com.team.domain.model.category.Category

sealed interface EditMyCategoriesUiEvent {

    data class SelectCategory(val category: Category): EditMyCategoriesUiEvent

    data class UnSelectCategory(val category: Category): EditMyCategoriesUiEvent

    data class MoveCategory(val categories: List<Category>): EditMyCategoriesUiEvent

    data object SelectAll: EditMyCategoriesUiEvent
}