package com.team.data.network.model.request

import com.squareup.moshi.JsonClass
import com.team.domain.model.account.NicknameValidation

@JsonClass(generateAdapter = true)
data class NicknameValidationRequest(
    val nickname: String,
)

fun NicknameValidation.toNicknameValidationRequest(): NicknameValidationRequest =
    NicknameValidationRequest(nickname)
