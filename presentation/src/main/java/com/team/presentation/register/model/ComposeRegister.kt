package com.team.presentation.register.model

import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile

data class ComposeRegister(
    val accountId: String,
    val accountName: String,
    val profile: ComposeRegisterProfile,
) {
    constructor() : this("", "", ComposeRegisterProfile())
}

data class ComposeRegisterProfile(
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
) {
    constructor() : this("", "", "")
}

fun Register.toComposeModel(): ComposeRegister =
    ComposeRegister(accountId, accountName, profile.toComposeModel())

fun RegisterProfile.toComposeModel(): ComposeRegisterProfile =
    ComposeRegisterProfile(profileId, nickname, photoUrl)
