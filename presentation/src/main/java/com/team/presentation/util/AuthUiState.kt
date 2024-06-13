package com.team.presentation.util

import com.team.domain.util.ErrorType

sealed class AuthUiState {
    data object Loading: AuthUiState()
    data class Success<T> (val data: T? = null): AuthUiState()
    data class Error (val errorType: ErrorType? = null): AuthUiState()
}