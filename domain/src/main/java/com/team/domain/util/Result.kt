package com.team.domain.util

typealias BaseError = Error

/** Result Wrapper Class
 *
 * 1. When used in NetworkCall: Not Used 'Loading()'
 * 2. When used in Repository: Use 'Loading()' **/
sealed interface Result<out T, out E: BaseError> {
    data class Success<out T, out E: BaseError>(val data: T): Result<T, E>

    /** error: 매핑된 에러 메시지, message: 서버 혹은 원본 에러 메시지 **/
    data class Error<out T, out E: BaseError>(val error: E, val message: String? = null): Result<T, E>
    data object Loading: Result<Nothing, Nothing>
}