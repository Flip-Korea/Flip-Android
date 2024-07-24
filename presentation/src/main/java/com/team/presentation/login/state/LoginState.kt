package com.team.presentation.login.state

import com.team.presentation.util.UiText

data class LoginState(
    val accountExists: Boolean? = null,
    val loading: Boolean = false,
    val error: UiText? = null
)
