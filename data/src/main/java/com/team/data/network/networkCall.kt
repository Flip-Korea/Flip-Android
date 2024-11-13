package com.team.data.network

import android.util.MalformedJsonException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.util.retry
import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * 모든 네트워크 호출에 사용 되는 제네릭 함수
 *
 * @param call suspend function 이어야 하며, 반환 값은 Response 타입
 *
 * @return Result<T, ErrorType>
 * @see ErrorType
 */
suspend fun <T> networkCall(
    call: suspend () -> Response<T>
): Result<T, ErrorType> {

    val toNetworkErrorType = { code: Int ->
        when (code) {
            400 -> ErrorType.Network.BAD_REQUEST
            401 -> ErrorType.Network.UNAUTHORIZED
            403 -> ErrorType.Network.FORBIDDEN
            404 -> ErrorType.Network.NOT_FOUND
            500 -> ErrorType.Network.INTERNAL_SERVER_ERROR
            else -> {
                if (code/100 == 5) {
                    ErrorType.Network.UNEXPECTED_SERVER
                } else {
                    ErrorType.Network.UNEXPECTED
                }
            }
        }
    }

    try {
        val response = retry { call() }
        return if (response.body() != null && response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(ErrorBody::class.java)
            val errorBody = response.errorBody()?.source()?.let { source ->
                adapter.fromJson(source)
            }
            Result.Error(
                error = toNetworkErrorType(response.code()),
                errorBody = errorBody,
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
}