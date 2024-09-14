package com.team.presentation.tempflipbox

import com.team.domain.model.post.TempPost
import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.util.uitext.UiText

/**
 * 임시저장함을 위한 Contract Class
 */
class TempFlipBoxContract {
    sealed class UiState: BaseUiState {
        data object Idle: UiState()
        data object Loading: UiState()
        data class Error(val error: UiText): UiState()
        data class TempPosts(val tempPosts: List<TempPost> = emptyList()): UiState()
    }

    sealed class UiEvent: BaseUiEvent {
        /** @param tempPostIds null 이면 삭제모달창 표시, null이 아니면 삭제 진행*/
        data class OnTempPostsDelete(val tempPostIds: List<Long>? = null): UiEvent()
        data object NavigateToPostDetail: UiEvent()
        data object NavigateToBack: UiEvent()
        data object GetTempPosts: UiEvent()
    }

    sealed class UiEffect: BaseUiEffect {
        data object ShowDialogModal: UiEffect()
    }
}