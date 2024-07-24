package com.team.data.network.retrofit

import android.util.Log
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
): Interceptor {

    private val TAG = this.javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {

        Log.d(TAG, "Interceptor Triggered!")

        // get accessToken & just continue request when accessToken is null
        val accessToken = runBlocking {
            dataStoreManager.getStringData(DataStoreType.TokenType.ACCESS_TOKEN)
                .catch { emit("") }
                .first()
        } ?: return chain.proceed(chain.request())

        // accessToken is not null
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        val response = chain.proceed(requestBuilder.build())

        // Two situations in which the logic below continues to be executed:
        // 1. after Process(OkHttp's Authentication function) for HTTP Status 401
        // 2. just receive response to a request with token

        Log.d(TAG, "Interceptor Triggered! (Proceed Response (after)")

        if (response.isSuccessful) {
            when(response.code) {
                200 -> { Log.d(TAG, "Interceptor Code: 200 OK") }
                201 -> { Log.d(TAG, "Interceptor Code: 201 Created") }
                else -> { Log.d(TAG, "Interceptor Code: Anything Success") }
            }
        } else {
            // Failure (Ex. 4xx, 5xx)
            when(response.code) {
                401 -> { Log.d(TAG, "Interceptor Code: 401 UnAuthorized") } // pass
                404 -> { Log.d(TAG, "Interceptor Code: 404 Not Found") }
                else -> { Log.d(TAG, "Interceptor Code: Unexpected") }
            }
            Log.d(TAG, "request: ${response.request}\n" + "message: ${response.message}")
        }

        return response
    }
}