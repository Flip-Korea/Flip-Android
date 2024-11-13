package com.team.domain.util

data class ErrorBody(
    val code: String,
    val errors: List<ErrorBodyContent>?,
    val message: String
)

data class ErrorBodyContent(
    val field: String,
    val reason: String,
    val value: String
)