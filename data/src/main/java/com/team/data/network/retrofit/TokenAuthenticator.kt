package com.team.data.network.retrofit

import android.util.Log
import com.team.data.datastore.DataStoreManager
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/** Only Called Once When Receive HTTP Status Code 401. **/
class TokenAuthenticator @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val authNetworkApi: AccountNetworkApi,
) : Authenticator {

    private val TAG = this.javaClass.simpleName

    override fun authenticate(route: Route?, response: Response): Request? {

        Log.d(TAG, "Authenticator Triggered!")

        // get originalRefreshToken
        val originalRefreshToken = runBlocking {
            dataStoreManager.getToken(DataStoreManager.TokenType.REFRESH_TOKEN)
                .catch { emit("") }
                .first()
        }

        return runBlocking {
            // refreshTokens and get AccessToken & RefreshToken
            val newTokens = refreshToken(originalRefreshToken)
            Log.d(TAG, "Refresh Token (Response: ${newTokens.code()})")

            // couldn't refresh the token, so restart the login process
            if (!newTokens.isSuccessful || newTokens.body() == null) {
                dataStoreManager.deleteToken(DataStoreManager.TokenType.ACCESS_TOKEN)
                dataStoreManager.deleteToken(DataStoreManager.TokenType.REFRESH_TOKEN)
            }

            // Save Tokens
            // & Call With Tokens (can get response from 'chain.proceed(...)')
            newTokens.body()?.let { res ->
                dataStoreManager.saveToken(DataStoreManager.TokenType.ACCESS_TOKEN, res.accessToken)
                dataStoreManager.saveToken(DataStoreManager.TokenType.REFRESH_TOKEN, res.refreshToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${res.accessToken}")
                    .build()
            }
        }
    }

    private suspend fun refreshToken(originalRefreshToken: String?): retrofit2.Response<TokenResponse> =
        authNetworkApi.tokenRefresh("Bearer $originalRefreshToken")
        // token must start with 'Bearer'
        // refresh token from refresh api service
}