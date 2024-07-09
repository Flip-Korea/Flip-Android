package com.team.domain.util

typealias BaseError = Error

/** Result Wrapper Class
 *
 * 1. When used in NetworkCall: Not Used 'Loading()'
 * 2. When used in Repository: Use 'Loading()'
 */
sealed interface Result<out T, out E: BaseError> {
    data class Success<out T, out E: BaseError>(val data: T): Result<T, E>

    /** error: Flip 커스텀 에러 타입
     *
     *  message: 에러 메시지 (보통 Exception 발생 시)
     *
     *  errorBody: 에러 바디 (보통 Network 에러 발생 시)
     *  @see ErrorBody
     */
    data class Error<out T, out E: BaseError>(
        val error: E,
        val message: String? = null,
        val errorBody: ErrorBody? = null
    ): Result<T, E>
    data object Loading: Result<Nothing, Nothing>
}