package com.team.presentation.home

/** HomeScreen UI Event */
sealed interface HomeUiEvent {
    data object OnSearchClick: HomeUiEvent
    data object OnNotificationClick: HomeUiEvent
}