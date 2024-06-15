package com.team.presentation.login.util

import com.team.presentation.login.state.AuthUiState
import kotlinx.coroutines.flow.Flow

/**
 * AuthManager
 *
 * Used in SocialLogin(Google, Kakao)
 */
interface AuthManager {

    fun signIn(): Flow<AuthUiState>

    suspend fun signOut()

    suspend fun deleteAccount(): Flow<AuthUiState>
}