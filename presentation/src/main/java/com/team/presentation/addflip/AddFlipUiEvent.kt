package com.team.presentation.addflip

import com.team.domain.model.category.Category
import com.team.presentation.util.BackgroundColorType

sealed interface AddFlipUiEvent {

    data class OnSelectedCategoryChanged(val category: Category): AddFlipUiEvent

    data class OnSaveTempPost(
        val title: String,
        val content: List<String> = emptyList(),
        val selectedColor: BackgroundColorType,
        val tags: List<String> = emptyList(),
    ): AddFlipUiEvent

    data class OnSavePost(
        val title: String,
        val content: List<String> = emptyList(),
        val selectedColor: BackgroundColorType,
        val tags: List<String> = emptyList(),
    ): AddFlipUiEvent
}