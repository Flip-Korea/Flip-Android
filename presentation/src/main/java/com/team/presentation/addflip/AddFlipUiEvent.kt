package com.team.presentation.addflip

import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType

sealed interface AddFlipUiEvent {

    data class OnSelectedCategoryChanged(val category: Category): AddFlipUiEvent

    data class OnSafeSave(
        val title: String,
        val content: List<String>,
    ): AddFlipUiEvent

    data class OnSaveTempPost(
        val title: String,
        val content: List<String>,
        val selectedColor: BackgroundColorType,
        val tags: List<String> = emptyList(),
    ): AddFlipUiEvent

    data class OnSavePost(
        val title: String,
        val content: List<String>,
        val selectedColor: BackgroundColorType,
        val tags: List<String> = emptyList(),
    ): AddFlipUiEvent
}