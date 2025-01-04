package com.team.presentation.register.state

import com.team.presentation.util.uitext.UiText

sealed interface InputNameValidState {
    data object Idle : InputNameValidState

    data object Valid : InputNameValidState

    data class Invalid(
        val error: UiText?,
    ) : InputNameValidState
}

data class InputNameState(
    val name: String = "",
    val loading: Boolean = false,
    val inputNameValidState: InputNameValidState = InputNameValidState.Idle,
)
