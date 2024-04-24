package com.team.data.network

import android.util.MalformedJsonException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.team.data.util.retry
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException

/** 응답 값의 body가 있을 때만 사용 **/
fun <T> networkCallFlow(
    call: suspend () -> Response<T>
): Flow<Result<T, ErrorType>> = flow {

    val toNetworkErrorType = { code: Int ->
        when (code) {
            400 -> ErrorType.Network.BAD_REQUEST
            401 -> ErrorType.Network.UNAUTHORIZED
            403 -> ErrorType.Network.FORBIDDEN
            404 -> ErrorType.Network.NOT_FOUND
            408 -> ErrorType.Network.TIMEOUT
            429 -> ErrorType.Network.TOO_MANY_REQUESTS
            else -> {
                if (code/100 == 5) {
                    ErrorType.Network.SERVER_ERROR
                } else {
                    ErrorType.Network.UNEXPECTED
                }
            }
        }
    }

    // Main Logic

    emit(Result.Loading)

    try {
        val response = retry { call() }
        if (response.isSuccessful) {
            response.body()?.let { data ->
                emit(Result.Success(data))
            }
        } else {
            emit(
                Result.Error(
                    error = toNetworkErrorType(response.code()),
                    message = response.message()
                )
            )
        }
    } catch (e: HttpException) {
        emit(Result.Error(error = toNetworkErrorType(e.code()), message = e.message()))
    } catch (e: IOException) {
        emit(Result.Error(error = ErrorType.Exception.IO, message = e.localizedMessage))
    } catch (e: JsonDataException) {
        emit(Result.Error(ErrorType.Exception.JSON_DATA, message = e.localizedMessage))
    } catch (e: JsonEncodingException) {
        emit(Result.Error(ErrorType.Exception.JSON_ENCODING, message = e.localizedMessage))
    } catch (e: MalformedJsonException) {
        emit(Result.Error(ErrorType.Exception.MALFORMED_JSON, message = e.localizedMessage))
    } catch (e: TimeoutException) {
        emit(Result.Error(ErrorType.Exception.TIMEOUT, message = e.localizedMessage))
    } catch (e: Exception) {
        emit(Result.Error(ErrorType.Exception.EXCEPTION, message = e.localizedMessage))
    }
}