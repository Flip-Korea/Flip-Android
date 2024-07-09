package com.team.domain.util

// TODO 에러타입 확실히 재정의!
/**
 * Flip 에서 사용 되는 ErrorType
 */
sealed interface ErrorType: Error {
    enum class Network: ErrorType {
        /** 400 Bad Request */
        BAD_REQUEST,
        /** 401 Unauthorized OK */
        UNAUTHORIZED,
        /** 403 Forbidden */
        FORBIDDEN,
        /** 404 Not Found */
        NOT_FOUND,
        /** 500 Internal Server Error */
        INTERNAL_SERVER_ERROR,
        /** 알 수 없는 서버 오류 */
        UNEXPECTED_SERVER,
        /** 서버 오류는 아니지만 알 수 없는 오류 */
        UNEXPECTED,
        INTERNET_CONNECTION
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