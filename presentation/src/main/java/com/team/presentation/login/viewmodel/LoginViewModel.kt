package com.team.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.usecase.login.LoginUseCase
import com.team.domain.util.Result
import com.team.presentation.login.state.AuthUiState
import com.team.presentation.login.state.LoginState
import com.team.presentation.login.util.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun login(
        socialLoginPlatform: SocialLoginPlatform,
        authManager: AuthManager
    ) {
        authManager.signIn().onEach { authUiState ->
            when (authUiState) {
                AuthUiState.Loading -> {
                    _loginState.update { it.copy(loading = true) }
                }
                is AuthUiState.Error -> {
                    _loginState.update { it.copy(
                        loading = false,
                        error = authUiState.errorType
                    ) }
                }
                is AuthUiState.Success -> {
                    signIn(socialLoginPlatform, authUiState.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun signIn(
        socialLoginPlatform: SocialLoginPlatform,
        accountId: String,
    ) {
        loginUseCase(socialLoginPlatform, accountId).onEach { result ->
            when (result) {
                Result.Loading -> { }
                is Result.Error -> {
                    _loginState.update { it.copy(
                        loading = false,
                        error = result.error
                    ) }
                }
                is Result.Success -> {
                    _loginState.update { it.copy(
                        loading = false,
                        error = null,
                        accountExists = result.data
                    ) }
                }
            }
        }.launchIn(viewModelScope)
    }
}