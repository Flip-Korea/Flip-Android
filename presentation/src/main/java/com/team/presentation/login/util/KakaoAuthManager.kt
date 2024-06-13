package com.team.presentation.login.util

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.team.domain.util.ErrorType
import com.team.presentation.util.AuthUiState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * 'SignIn(Login) with Kakao' Manager Class
 * @param context context of declaration location (ex. applicationContext)
 */
class KakaoAuthManager(private val context: Context) : AuthManager {

    private val TAG = this.javaClass.simpleName

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            // Login Failed
        } else if (token != null) {
            // Login Success (ex -> token.accessToken)
        }
    }

    override fun signIn(): Flow<AuthUiState> = callbackFlow {
        trySend(AuthUiState.Loading)

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        // 1. Login Required
                        login(context)
                    } else {
                        // 2. another error
                        trySend(AuthUiState.Error(ErrorType.Auth.TOKEN_ERROR))
                        close()
                    }
                } else {
                    // 3. token validity check successful (renew if necessary)
                }
            }
        } else {
            // 1. Login Required
            login(context)
        }

        awaitClose {
            Log.d(TAG, "Login With Kakao Finished(maybe success or failure")
        }
    }

    override suspend fun signOut() {
        UserApiClient.instance.logout { e ->
        }
    }

    override suspend fun deleteAccount() {
        UserApiClient.instance.unlink { e ->
        }
    }

    private fun ProducerScope<AuthUiState>.login(context: Context) {

        // Check KakaoTalk installed
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk(context)
        } else {
            loginWithKakaoAccount(context)
        }
    }

    /** Login With KakaoTalk
     *
     * 1. error != null: error occurred
     * 2. token != null: Login Success
     * When KakaoTalk Login Failed, login with KakaoAccount
     */
    private fun ProducerScope<AuthUiState>.loginWithKakaoTalk(context: Context) =
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) { // Login Failed(with error)
                loginWithKakaoTalkError(context, error)
            } else if (token != null) { // Login Success
                loginSuccess()
            } else {
                trySend(AuthUiState.Error(ErrorType.Auth.UNEXPECTED))
                close()
            }
        }

    private fun ProducerScope<AuthUiState>.loginWithKakaoTalkError(
        context: Context,
        error: Throwable?,
    ) {
        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
            trySend(AuthUiState.Error(ErrorType.Auth.CANCELLED))
            close()
        } else {
            loginWithKakaoAccount(context)
        }
    }

    private fun ProducerScope<AuthUiState>.loginWithKakaoAccount(context: Context) =
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) { // Login Failed(with error)
                loginWithKakaoAccountError(error)
            } else if (token != null) { // Login Success
                loginSuccess()
            } else {
                trySend(AuthUiState.Error(ErrorType.Auth.UNEXPECTED))
            }
            close()
        }

    private fun ProducerScope<AuthUiState>.loginWithKakaoAccountError(error: Throwable?) {
        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
            trySend(AuthUiState.Error(ErrorType.Auth.CANCELLED))
            close()
        }
    }

    private fun ProducerScope<AuthUiState>.loginSuccess() {
        UserApiClient.instance.me { user, error ->
            user?.let { u ->
                trySend(AuthUiState.Success(u.id))
                close()
            } ?: {
                trySend(AuthUiState.Error(ErrorType.Auth.USER_NOT_FOUND))
                close()
            }
        }
    }
}