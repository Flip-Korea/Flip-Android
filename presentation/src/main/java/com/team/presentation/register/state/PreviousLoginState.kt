package com.team.presentation.register.state

import com.team.domain.model.account.Login

data class PreviousLoginState(
    val login: Login? = null,
)
