package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.account.ProfileIdValidation

@JsonClass(generateAdapter = true)
data class ProfileIdValidationRequest(
    @Json(name = "userId") val profileId: String,
)

fun ProfileIdValidation.toProfileIdValidationRequest(): ProfileIdValidationRequest =
    ProfileIdValidationRequest(profileId)
