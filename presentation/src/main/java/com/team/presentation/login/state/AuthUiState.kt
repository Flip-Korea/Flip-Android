package com.team.presentation.login.state

import com.team.domain.util.ErrorType

sealed class AuthUiState {
    data object Loading: AuthUiState()
    data class Success (val data: String): AuthUiState()
    data class Error (val errorType: ErrorType? = null): AuthUiState()
}