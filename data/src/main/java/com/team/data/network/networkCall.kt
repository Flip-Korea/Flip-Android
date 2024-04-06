package com.team.data.network

import android.util.MalformedJsonException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException

suspend fun <T> networkCall(
    call: suspend () -> Response<T>
): Result<T, ErrorType> {

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
    try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Result.Success(data)
            }
        } else {
            return Result.Error(
                error = toNetworkErrorType(response.code()),
                message = response.message()
            )
        }
    } catch (e: HttpException) {
        return Result.Error(error = toNetworkErrorType(e.code()), message = e.message())
    } catch (e: IOException) {
        return Result.Error(error = ErrorType.Exception.IO, message = e.localizedMessage)
    } catch (e: JsonDataException) {
        return Result.Error(ErrorType.Exception.JSON_DATA, message = e.localizedMessage)
    } catch (e: JsonEncodingException) {
        return Result.Error(ErrorType.Exception.JSON_ENCODING, message = e.localizedMessage)
    } catch (e: MalformedJsonException) {
        return Result.Error(ErrorType.Exception.MALFORMED_JSON, message = e.localizedMessage)
    } catch (e: TimeoutException) {
        return Result.Error(ErrorType.Exception.TIMEOUT, message = e.localizedMessage)
    } catch (e: Exception) {
        return Result.Error(ErrorType.Exception.EXCEPTION, message = e.localizedMessage)
    }

    return Result.Error(ErrorType.Network.UNEXPECTED)
}