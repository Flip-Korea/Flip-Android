package com.team.presentation.tempflipbox

import com.team.domain.model.post.TempPost
import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.util.uitext.UiText

//TODO: Error는 어디에 들어가야 하나?
interface TempFlipBoxContract {
    sealed class UiState: BaseUiState {
        data object Idle: UiState()
        data object Loading: UiState()
        data class Error(val error: UiText): UiState()
        data class TempPosts(val tempPosts: List<TempPost> = emptyList()): UiState()
    }

    sealed interface UiEvent: BaseUiEvent {
        data class OnTempPostsDelete(val tempPostIds: List<Long>): UiEvent
        data object NavigateToPostDetail: UiEvent
        data object NavigateToBack: UiEvent
        data object GetTempPosts: UiEvent
    }

    sealed interface UiEffect: BaseUiEffect {

    }
}