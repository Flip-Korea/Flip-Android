package com.team.presentation.util

import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.R

fun Result.Error<*, ErrorType>.asUiText(): UiText = error.asUiText()

fun ErrorType.asUiText(): UiText =
    when (this) {
        /** Exception */
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

        /** Network */
        ErrorType.Network.BAD_REQUEST -> UiText.StringResource(R.string.error_network_bad_request)
        ErrorType.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_network_unauthorized)
        ErrorType.Network.FORBIDDEN -> UiText.StringResource(R.string.error_network_forbidden)
        ErrorType.Network.NOT_FOUND -> UiText.StringResource(R.string.error_network_not_found)
        ErrorType.Network.INTERNAL_SERVER_ERROR -> UiText.StringResource(R.string.error_network_internal_server)
        ErrorType.Network.UNEXPECTED_SERVER -> UiText.StringResource(R.string.error_network_unexpected_server)
        ErrorType.Network.UNEXPECTED -> UiText.StringResource(R.string.error_network_unexpected)
        ErrorType.Network.INTERNET_CONNECTION -> UiText.StringResource(R.string.error_network_internet_connection)

        /** Local */
        ErrorType.Local.EMPTY -> UiText.StringResource(R.string.error_local_empty)

        ErrorType.Local.UNEXPECTED -> UiText.StringResource(
            R.string.error_local_unexpected
        )

        /** Token */
        ErrorType.Token.NOT_FOUND -> UiText.StringResource(
            R.string.error_token_not_found
        )

        /** Auth */
        ErrorType.Auth.PARSING_EXCEPTION -> UiText.StringResource(
            R.string.error_auth_parsing_exception
        )

        ErrorType.Auth.CREDENTIAL_TYPE_INVALID -> UiText.StringResource(
            R.string.error_auth_credential_type_invalid
        )

        ErrorType.Auth.TOKEN_ERROR -> UiText.StringResource(
            R.string.error_auth_token_error
        )

        ErrorType.Auth.CANCELLED -> UiText.StringResource(
            R.string.error_auth_cancelled
        )

        ErrorType.Auth.DELETE_ACCOUNT_FAILED -> UiText.StringResource(
            R.string.error_auth_delete_acocunt_failed
        )

        ErrorType.Auth.USER_NOT_FOUND -> UiText.StringResource(
            R.string.error_auth_user_not_found
        )

        ErrorType.Auth.UNEXPECTED -> UiText.StringResource(
            R.string.error_auth_unexpected
        )
    }