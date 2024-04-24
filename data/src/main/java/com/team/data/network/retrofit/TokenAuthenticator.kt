package com.team.data.network.retrofit

import android.util.Log
import com.team.data.datastore.TokenManager
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.retrofit.api.AuthNetworkApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/** Only Called Once When Receive HTTP Status Code 401. **/
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authNetworkApi: AuthNetworkApi,
) : Authenticator {

    private val TAG = this.javaClass.simpleName

    override fun authenticate(route: Route?, response: Response): Request? {

        Log.d(TAG, "Authenticator Triggered!")

        // get originalRefreshToken
        val originalRefreshToken = runBlocking {
            tokenManager.getToken(TokenManager.Type.REFRESH_TOKEN).first()
        }

        return runBlocking {
            // refreshTokens and get AccessToken & RefreshToken
            val newTokens = refreshToken(originalRefreshToken)
            Log.d(TAG, "Refresh Token (Response: ${newTokens.code()})")

            // couldn't refresh the token, so restart the login process
            if (!newTokens.isSuccessful || newTokens.body() == null) {
//                tokenManager.deleteToken(TokenManager.Type.ACCESS_TOKEN)
//                tokenManager.deleteToken(TokenManager.Type.REFRESH_TOKEN)
            }

            // Save Tokens
            // & Call With Tokens (can get response from 'chain.proceed(...)')
            newTokens.body()?.let { res ->
                tokenManager.saveToken(TokenManager.Type.ACCESS_TOKEN, res.accessToken)
                tokenManager.saveToken(TokenManager.Type.REFRESH_TOKEN, res.refreshToken)
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