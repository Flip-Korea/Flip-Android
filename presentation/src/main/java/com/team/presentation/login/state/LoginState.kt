package com.team.presentation.login.state

import com.team.domain.util.ErrorType

data class LoginState(
    val accountExists: Boolean? = null,
    val loading: Boolean = false,
    val error: ErrorType? = null
)
