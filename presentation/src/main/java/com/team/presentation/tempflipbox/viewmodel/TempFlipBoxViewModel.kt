package com.team.presentation.tempflipbox.viewmodel

import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.tempflipbox.TempFlipBoxContract

class TempFlipBoxViewModel(

): FlipBaseViewModel<TempFlipBoxContract.UiState, TempFlipBoxContract.UiEvent, TempFlipBoxContract.UiEffect>() {

    override fun createInitialState(): TempFlipBoxContract.UiState = TempFlipBoxContract.UiState.Idle

    override suspend fun handleEvent(event: TempFlipBoxContract.UiEvent) {
        when (event) {
            TempFlipBoxContract.UiEvent.NavigateToBack -> TODO()
            TempFlipBoxContract.UiEvent.NavigateToPostDetail -> TODO()
            is TempFlipBoxContract.UiEvent.OnTempPostsDelete -> TODO()
        }
    }
}