package com.team.domain.model.account

import javax.inject.Inject

data class ProfileIdValidation(
    val profileId: String,
)

class ProfileIdValidationFactory @Inject constructor() {
    fun create(profileId: String): ProfileIdValidation = ProfileIdValidation(profileId)
}
