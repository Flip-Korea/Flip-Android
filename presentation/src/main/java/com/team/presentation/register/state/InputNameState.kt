package com.team.presentation.register.state

import com.team.presentation.util.uitext.UiText

data class InputNameState(
    val name: String = "",
    val loading: Boolean = false,
    val error: UiText? = null
)
