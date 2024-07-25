package com.team.presentation.home

/** Flip Card 만을 위한 UI Event */
sealed interface FlipCardUiEvent {
    data object OnLikeClick: FlipCardUiEvent
    data object OnCommentClick: FlipCardUiEvent
    data object OnScrapClick: FlipCardUiEvent
    data object OnFlipCardClick: FlipCardUiEvent
}