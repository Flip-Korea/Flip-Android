package com.team.presentation.login.state

data class LoginState(
    val accountExists: Boolean? = null,
    val loading: Boolean = false,
)
