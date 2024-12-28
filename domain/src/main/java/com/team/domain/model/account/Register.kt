package com.team.domain.model.account

data class Register(
    val accountId: String,
    val accountName: String,
    val profile: RegisterProfile,
)

data class RegisterProfile(
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
)
