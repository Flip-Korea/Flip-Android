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
        UNEXPECTED
    }

    enum class Local: ErrorType {

    }

    enum class Exception: ErrorType {
        IO,
        TIMEOUT,
        JSON_DATA,
        JSON_ENCODING,
        MALFORMED_JSON,
        EXCEPTION
    }
}