package com.team.presentation.addflip.state

import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.presentation.common.state.ModalState
import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.util.uitext.UiText

class AddFlipContract {
    sealed class UiState : BaseUiState {
        data class Error(val error: UiText) : UiState()

        data class Content(
            val newPostState: NewPostState = NewPostState(),
            val postSaveState: PostSaveState = PostSaveState(),
            val categories: List<Category> = emptyList(),
        ) : UiState()
    }

    sealed class UiEvent : BaseUiEvent {
        data class OnTitleChanged(val title: String) : UiEvent()

        data class OnContentsChanged(val contents: List<String>) : UiEvent()

        data class OnBackgroundColorChanged(val bgColorType: BackgroundColorType) : UiEvent()

        data class OnCategoryChanged(val category: Category) : UiEvent()

        data class OnPageDelete(val complete: Boolean) : UiEvent()

        data object SaveTempPost: UiEvent()

        data object SavePost : UiEvent()

        data object SafeNavigateBack : UiEvent()

        data object NavigateBack : UiEvent()
    }

    sealed class UiEffect : BaseUiEffect {
        data class ShowPageDeleteWarningModal(val modalState: ModalState) : UiEffect()
        data class NavigateBack(val safeSave: Boolean) : UiEffect()
    }
}
