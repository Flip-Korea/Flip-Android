package com.team.presentation.login.state

import com.team.domain.model.account.Login

data class LoginState(
    val loginInfo: Login? = null,
    val accountExists: Boolean? = null,
    val loading: Boolean = false,
)
