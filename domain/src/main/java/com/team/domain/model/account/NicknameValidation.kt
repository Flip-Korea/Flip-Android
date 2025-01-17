package com.team.domain.model.account

import javax.inject.Inject

data class NicknameValidation(
    val nickname: String,
)

class NicknameValidationFactory @Inject constructor() {
    fun create(nickname: String): NicknameValidation = NicknameValidation(nickname)
}
