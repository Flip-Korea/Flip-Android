package com.team.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.account.Login
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.usecase.login.LoginUseCase
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.NavigationItem
import com.team.presentation.login.state.AuthUiState
import com.team.presentation.login.state.LoginState
import com.team.presentation.login.util.AuthManager
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import com.team.presentation.util.uitext.errorBodyFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _errorEffect = Channel<UiText>()
    val errorEffect = _errorEffect.receiveAsFlow()

    private val _navigateEffect = Channel<NavigationItem>()
    val navigateEffect = _navigateEffect.receiveAsFlow()

    fun login(
        socialLoginPlatform: SocialLoginPlatform,
        authManager: AuthManager,
    ) {
        authManager
            .signIn()
            .onEach { authUiState ->
                when (authUiState) {
                    AuthUiState.Loading -> {
                        _loginState.update { it.copy(loading = true) }
                    }

                    is AuthUiState.Error -> {
                        _loginState.update { it.copy(loading = false) }
                        val error =
                            authUiState.errorType?.asUiText()
                                ?: ErrorType.Network.UNEXPECTED.asUiText()
                        _errorEffect.send(error)
                    }

                    is AuthUiState.Success -> {
                        _loginState.update {
                            it.copy(loginInfo = Login(socialLoginPlatform, authUiState.data))
                        }
                        signIn(socialLoginPlatform, authUiState.data)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun signIn(
        socialLoginPlatform: SocialLoginPlatform,
        accountId: String,
    ) {
        val login = Login(socialLoginPlatform, accountId)

        loginUseCase(login)
            .onEach { result ->
                when (result) {
                    Result.Loading -> {
                        _loginState.update { it.copy(loading = true) }
                    }

                    is Result.Error -> {
                        _loginState.update { it.copy(loading = false) }
                        processSignInError(result)
                    }

                    is Result.Success -> {
                        _loginState.update {
                            it.copy(
                                loading = false,
                                accountExists = result.data,
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private suspend fun processSignInError(result: Result.Error<Boolean, ErrorType>) {
        if (result.httpStatusCode != null &&
            result.httpStatusCode == HttpURLConnection.HTTP_NOT_FOUND
        ) {
            _navigateEffect.send(NavigationItem.RegisterNav)
            return
        }

        _errorEffect.send(result.errorBodyFirst())
    }
}
