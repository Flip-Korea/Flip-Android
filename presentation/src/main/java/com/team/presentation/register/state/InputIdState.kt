package com.team.presentation.register.state

import com.team.presentation.util.uitext.UiText

sealed interface InputIdValidState {
    data object Idle : InputIdValidState

    data object Valid : InputIdValidState

    data class Invalid(
        val error: UiText?,
    ) : InputIdValidState
}

data class InputIdState(
    val id: String = "",
    val loading: Boolean = false,
    val inputIdValidState: InputIdValidState = InputIdValidState.Idle,
)
