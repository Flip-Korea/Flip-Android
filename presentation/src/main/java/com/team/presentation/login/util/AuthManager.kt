package com.team.presentation.login.util

import com.team.presentation.util.AuthUiState
import kotlinx.coroutines.flow.Flow

/**
 * AuthManager
 *
 * Used in SocialLogin(Google, Kakao)
 */
interface AuthManager {

    fun signIn(): Flow<AuthUiState>

    suspend fun signOut()

    suspend fun deleteAccount()
}