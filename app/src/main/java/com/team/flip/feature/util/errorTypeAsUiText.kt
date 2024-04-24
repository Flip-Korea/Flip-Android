package com.team.flip.feature.util

import com.team.domain.util.ErrorType
import com.team.flip.R
import com.team.domain.util.Result

fun Result.Error<*, ErrorType>.asUiText(): UiText = error.asUiText()

fun ErrorType.asUiText(): UiText =
    when (this) {
        // Exception
        ErrorType.Exception.IO -> UiText.StringResource(
            R.string.error_exception_io
        )
        ErrorType.Exception.TIMEOUT -> UiText.StringResource(
            R.string.error_exception_timeout
        )
        ErrorType.Exception.JSON_DATA -> UiText.StringResource(
            R.string.error_exception_json_data
        )
        ErrorType.Exception.JSON_ENCODING -> UiText.StringResource(
            R.string.error_exception_json_encoding
        )
        ErrorType.Exception.MALFORMED_JSON -> UiText.StringResource(
            R.string.error_exception_malformed_json
        )
        ErrorType.Exception.EXCEPTION -> UiText.StringResource(
            R.string.error_exception_exception
        )

        // Network
        ErrorType.Network.BAD_REQUEST -> UiText.StringResource(
            R.string.error_network_bad_request
        )
        ErrorType.Network.UNAUTHORIZED -> UiText.StringResource(
            R.string.error_network_unauthorized
        )
        ErrorType.Network.FORBIDDEN -> UiText.StringResource(
            R.string.error_network_forbidden
        )
        ErrorType.Network.NOT_FOUND -> UiText.StringResource(
            R.string.error_network_not_found
        )
        ErrorType.Network.TIMEOUT -> UiText.StringResource(
            R.string.error_network_timeout
        )
        ErrorType.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.error_network_too_many_requests
        )
        ErrorType.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.error_network_server_error
        )
        ErrorType.Network.INTERNET_CONNECTION -> UiText.StringResource(
            R.string.error_network_internet_connection
        )
        ErrorType.Network.SERIALIZATION -> UiText.StringResource(
            R.string.error_network_serialization
        )
        ErrorType.Network.CONFLICT -> UiText.StringResource(
            R.string.error_network_conflict
        )
        ErrorType.Network.UNEXPECTED -> UiText.StringResource(
            R.string.error_network_unexpected
        )

        // Local
        ErrorType.Local.EMPTY -> UiText.StringResource(
            R.string.error_local_empty
        )
        ErrorType.Local.UNEXPECTED -> UiText.StringResource(
            R.string.error_local_unexpected
        )

        // Token
        ErrorType.Token.NOT_FOUND -> UiText.StringResource(
            R.string.error_token_not_found
        )
    }