package com.team.presentation.login.util

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.team.domain.util.ErrorType
import com.team.presentation.BuildConfig
import com.team.presentation.login.state.AuthUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/**
 * 'SignIn(Login) with Google' Manager Class
 *
 * - Used Firebase Auth
 * @param context context of declaration location (ex. applicationContext)
 * @param credentialManager CredentialManager(Android Jetpack API)
 */
class GoogleAuthManager(
    private val context: Context,
    private val credentialManager: CredentialManager,
): AuthManager {

    private val auth = Firebase.auth

    override fun signIn(): Flow<AuthUiState> = flow {
        emit(AuthUiState.Loading)

        val googleIdOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                            val googleIdToken = googleIdTokenCredential.idToken
                            val userResult = signInWithFirebase(googleIdToken)

                            userResult?.let { user ->
                                emit(AuthUiState.Success(user.uid))
                            } ?: emit(AuthUiState.Error(ErrorType.Auth.USER_NOT_FOUND))
                        } catch (e: GoogleIdTokenParsingException) {
                            emit(AuthUiState.Error(ErrorType.Auth.PARSING_EXCEPTION))
                        } catch (e: Exception) {
                            emit(AuthUiState.Error(ErrorType.Auth.UNEXPECTED))
                        }
                    }
                }

                else -> { emit(AuthUiState.Error(ErrorType.Auth.CREDENTIAL_TYPE_INVALID)) }
            }
        } catch (e: GetCredentialCancellationException) {
            emit(AuthUiState.Error(ErrorType.Auth.CANCELLED))
        } catch (e: Exception) {
            emit(AuthUiState.Error(ErrorType.Auth.UNEXPECTED))
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
    }

    override suspend fun deleteAccount(): Flow<AuthUiState> = flow {
        emit(AuthUiState.Loading)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                val task = currentUser.delete()
                if (task.isComplete && task.isSuccessful) {
                    credentialManager.clearCredentialState(
                        ClearCredentialStateRequest()
                    )
                    emit(AuthUiState.Success("success"))
                } else {
                    emit(AuthUiState.Error(ErrorType.Auth.DELETE_ACCOUNT_FAILED))
                }
            } catch (e: Exception) {
                emit(AuthUiState.Error(ErrorType.Auth.UNEXPECTED))
            }
        } else {
            emit(AuthUiState.Error(ErrorType.Auth.USER_NOT_FOUND))
        }
    }

    private suspend fun signInWithFirebase(googleIdToken: String): FirebaseUser? {
        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return auth.signInWithCredential(firebaseCredential).await().user
    }
}