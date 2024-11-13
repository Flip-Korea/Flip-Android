package com.team.data.network.model.response

import com.squareup.moshi.JsonClass
import com.team.domain.util.ErrorBody

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val code: String,
    val errors: List<ErrorContentResponse>?,
    val message: String
)

fun ErrorResponse.toDomainModel(): ErrorBody =
    ErrorBody(
        code,
        errors?.map { it.toDomainModel() },
        message
    )