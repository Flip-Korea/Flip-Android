package com.team.presentation.register.state

import com.team.presentation.util.uitext.UiText

sealed interface InputNicknameValidState {
    data object Idle : InputNicknameValidState

    data object Valid : InputNicknameValidState

    data class Invalid(
        val error: UiText?,
    ) : InputNicknameValidState
}

data class InputNicknameState(
    val name: String = "",
    val loading: Boolean = false,
    val inputNicknameValidState: InputNicknameValidState = InputNicknameValidState.Idle,
)
