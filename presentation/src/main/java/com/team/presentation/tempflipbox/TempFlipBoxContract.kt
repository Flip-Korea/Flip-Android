package com.team.presentation.tempflipbox

import com.team.domain.model.post.TempPost
import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState

//TODO: Error는 어디에 들어가야 하나?
interface TempFlipBoxContract {
    sealed class UiState: BaseUiState {
        data object Idle: UiState()
        data object Loading: UiState()
        data class TempPosts(val tempPosts: List<TempPost> = emptyList()): UiState()
    }

    sealed interface UiEvent: BaseUiEvent {
        data class OnTempPostsDelete(val tempPosts: List<TempPost>): UiEvent
        data object NavigateToPostDetail: UiEvent
        data object NavigateToBack: UiEvent
    }

    sealed interface UiEffect: BaseUiEffect {

    }
}