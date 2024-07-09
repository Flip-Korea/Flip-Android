package com.team.data.network.model.response

import com.squareup.moshi.JsonClass
import com.team.domain.util.ErrorBodyContent

@JsonClass(generateAdapter = true)
data class ErrorContentResponse(
    val field: String,
    val reason: String,
    val value: String
)

fun ErrorContentResponse.toDomainModel(): ErrorBodyContent =
    ErrorBodyContent(field, reason, value)