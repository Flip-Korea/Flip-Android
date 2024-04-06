package com.team.flip.feature.util

import com.team.flip.R
import com.team.domain.util.Result

fun Result.Error<*, com.team.domain.util.ErrorType>.asUiText(): UiText = error.asUiText()

fun com.team.domain.util.ErrorType.asUiText(): UiText =
    when (this) {
        // Exception
        com.team.domain.util.ErrorType.Exception.IO -> UiText.StringResource(
            R.string.error_exception_io
        )
        com.team.domain.util.ErrorType.Exception.TIMEOUT -> UiText.StringResource(
            R.string.error_exception_timeout
        )
        com.team.domain.util.ErrorType.Exception.JSON_DATA -> UiText.StringResource(
            R.string.error_exception_json_data
        )
        com.team.domain.util.ErrorType.Exception.JSON_ENCODING -> UiText.StringResource(
            R.string.error_exception_json_encoding
        )
        com.team.domain.util.ErrorType.Exception.MALFORMED_JSON -> UiText.StringResource(
            R.string.error_exception_malformed_json
        )
        com.team.domain.util.ErrorType.Exception.EXCEPTION -> UiText.StringResource(
            R.string.error_exception_exception
        )

        // Network
        com.team.domain.util.ErrorType.Network.BAD_REQUEST -> UiText.StringResource(
            R.string.error_network_bad_request
        )
        com.team.domain.util.ErrorType.Network.UNAUTHORIZED -> UiText.StringResource(
            R.string.error_network_unauthorized
        )
        com.team.domain.util.ErrorType.Network.FORBIDDEN -> UiText.StringResource(
            R.string.error_network_forbidden
        )
        com.team.domain.util.ErrorType.Network.NOT_FOUND -> UiText.StringResource(
            R.string.error_network_not_found
        )
        com.team.domain.util.ErrorType.Network.TIMEOUT -> UiText.StringResource(
            R.string.error_network_timeout
        )
        com.team.domain.util.ErrorType.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.error_network_too_many_requests
        )
        com.team.domain.util.ErrorType.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.error_network_server_error
        )
        com.team.domain.util.ErrorType.Network.INTERNET_CONNECTION -> UiText.StringResource(
            R.string.error_network_internet_connection
        )
        com.team.domain.util.ErrorType.Network.SERIALIZATION -> UiText.StringResource(
            R.string.error_network_serialization
        )
        com.team.domain.util.ErrorType.Network.UNEXPECTED -> UiText.StringResource(
            R.string.error_network_unexpected
        )

        // Local
    }