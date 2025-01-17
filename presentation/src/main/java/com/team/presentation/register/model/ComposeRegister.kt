package com.team.presentation.register.model

import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile
import com.team.domain.type.SocialLoginPlatform

data class ComposeRegister(
    val socialLoginPlatform: SocialLoginPlatform,
    val oauthId: String,
    val profile: ComposeRegisterProfile,
    val adsAgree: Boolean,
) {
    constructor() : this(SocialLoginPlatform.Google, "", ComposeRegisterProfile(), false)
}

data class ComposeRegisterProfile(
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
) {
    constructor() : this("", "", "")
}

fun Register.toComposeModel(): ComposeRegister =
    ComposeRegister(
        socialLoginPlatform,
        oauthId,
        profile.toComposeModel(),
        adsAgree,
    )

fun RegisterProfile.toComposeModel(): ComposeRegisterProfile =
    ComposeRegisterProfile(userId, nickname, photoUrl)
