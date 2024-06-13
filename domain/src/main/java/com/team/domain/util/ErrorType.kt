package com.team.domain.util

// TODO 에러타입 확실히 재정의!
sealed interface ErrorType: Error {
    enum class Network: ErrorType {
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        TIMEOUT,
        TOO_MANY_REQUESTS,
        SERVER_ERROR,
        INTERNET_CONNECTION,
        SERIALIZATION,
        CONFLICT,
        UNEXPECTED
    }

    enum class Local: ErrorType {
        EMPTY,
        UNEXPECTED
    }

    enum class Exception: ErrorType {
        IO,
        TIMEOUT,
        JSON_DATA,
        JSON_ENCODING,
        MALFORMED_JSON,
        EXCEPTION
    }

    enum class Token: ErrorType {
        NOT_FOUND,
    }

    enum class Auth: ErrorType {
        PARSING_EXCEPTION,
        CREDENTIAL_TYPE_INVALID,
        TOKEN_ERROR,
        CANCELLED,
        DELETE_ACCOUNT_FAILED,
        USER_NOT_FOUND,
        UNEXPECTED,
    }
}