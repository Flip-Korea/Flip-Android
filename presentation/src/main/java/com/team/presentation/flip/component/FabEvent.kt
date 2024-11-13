package com.team.presentation.flip.component

/** Flip Fab 이벤트 */
sealed interface FabEvent {
    data object OnLikeClick: FabEvent
    data object OnScrapClick: FabEvent
    data object OnCommentClick: FabEvent
    data object OnMoreClick: FabEvent
}