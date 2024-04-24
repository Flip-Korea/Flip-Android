package com.team.flip.feature.ui_test.token_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.flip.feature.util.UiText
import com.team.flip.feature.util.asUiText
import com.team.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
//    val user: User? = null,
    val body: UiText = UiText.DynamicString(""),
    val loading: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    // 나중에는 DataSource 직접 가져다 쓰지말고
    // UseCase 클래스나 리포지토리를 진입점으로 교체
    private val authNetworkDataSource: com.team.data.network.source.AuthNetworkDataSource,
    private val userNetworkDataSource: com.team.data.network.source.UserNetworkDataSource,
    private val tokenManager: com.team.data.datastore.TokenManager
): ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun register() {
        viewModelScope.launch {

            // 임시 모의 개체
            val networkRegister = com.team.data.network.model.request.NetworkRegister(
                accountId = "kakao123test",
                name = "testName",
                categories = listOf(1, 2, 3),
                profile = com.team.data.network.model.request.NetworkProfile(
                    profileId = "honggd",
                    nickname = "honggildong",
                    photoUrl = "test.com"
                )
            )

            authNetworkDataSource.register(networkRegister).onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _loginState.update { it.copy(
                            loading = false,
                            body = result.error.asUiText()
                        ) }
                    }
                    Result.Loading -> { _loginState.update { it.copy(loading = true) } }
                    is Result.Success -> {
                        _loginState.update { it.copy(
                            loading = false,
                            body = UiText.DynamicString(makePrettyBody(result.data))
                        ) }

                        /** 여기서 저장 해도 되나? **/
                        saveToken(result.data.accessToken, result.data.refreshToken)
                    }
                }
            }.launchIn(this)
        }
    }

    fun login(accountId: String) {
        // 일단 임시로 입력 값 X
        viewModelScope.launch {

            authNetworkDataSource.login(accountId).onEach { result ->
                when(result) {
                    Result.Loading -> { _loginState.update { it.copy(loading = true) } }
                    is Result.Error -> {
                        _loginState.update { it.copy(
                            loading = false,
                            body = result.error.asUiText()
                        ) }
                    }
                    is Result.Success -> {
                        _loginState.update { it.copy(
                            body = UiText.DynamicString(makePrettyBody(result.data)),
                            loading = false
                        ) }

                        /** 여기서 저장 해도 되나? **/
                        saveToken(result.data.accessToken, result.data.refreshToken)
                    }
                }
            }.launchIn(this)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.update { it.copy(loading = true) }

            tokenManager.deleteToken(com.team.data.datastore.TokenManager.Type.ACCESS_TOKEN)
            tokenManager.deleteToken(com.team.data.datastore.TokenManager.Type.REFRESH_TOKEN)

            val accessTokenByDataStore =
                tokenManager.getToken(com.team.data.datastore.TokenManager.Type.ACCESS_TOKEN).first() ?: "null"
            val refreshTokenByDataStore =
                tokenManager.getToken(com.team.data.datastore.TokenManager.Type.REFRESH_TOKEN).first() ?: "null"

            _loginState.update { it.copy(
                loading = false,
                body = UiText.DynamicString(
                    "(Token By DataStore)\n\n" +
                            "accessToken: ${accessTokenByDataStore}\n\n" +
                            "refreshToken: $refreshTokenByDataStore"
                )
            ) }
        }
    }

    fun getProfile(profileId: String) {
        viewModelScope.launch {
            userNetworkDataSource.getProfile(profileId).onEach { result ->
                when(result) {
                    Result.Loading -> { _loginState.update { it.copy(loading = true) } }
                    is Result.Error -> {
                        _loginState.update { it.copy(
                            loading = false,
                            body = result.error.asUiText()
                        ) }
                    }
                    is Result.Success -> {
                        _loginState.update { it.copy(
                            body = UiText.DynamicString(
                                "profileId: ${result.data.profileId}\nnickname: ${result.data.nickname}"
                            ),
                            loading = false
                        ) }
                    }
                }
            }.launchIn(this)
        }
    }

    fun getTokenByDataStore() {
        viewModelScope.launch {
            _loginState.update { it.copy(loading = true) }

            val accessTokenByDataStore =
                tokenManager.getToken(com.team.data.datastore.TokenManager.Type.ACCESS_TOKEN).first() ?: "null"
            val refreshTokenByDataStore =
                tokenManager.getToken(com.team.data.datastore.TokenManager.Type.REFRESH_TOKEN).first() ?: "null"

            _loginState.update { it.copy(
                loading = false,
                body = UiText.DynamicString(
                    "(Token By DataStore)\n\n" +
                            "accessToken: ${accessTokenByDataStore}\n\n" +
                            "refreshToken: $refreshTokenByDataStore"
                )
            ) }
        }
    }

    /** 여기서 저장 해도 되나? **/
    private fun saveToken(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            tokenManager.saveToken(com.team.data.datastore.TokenManager.Type.ACCESS_TOKEN, accessToken)
            tokenManager.saveToken(com.team.data.datastore.TokenManager.Type.REFRESH_TOKEN, refreshToken)
        }
    }

    private fun makePrettyBody(networkResponseLogin: com.team.data.network.model.response.TokenResponse?): String {
        return if (networkResponseLogin != null) {
                    "accessToken: ${networkResponseLogin.accessToken}\n\n" +
                    "refreshToken: ${networkResponseLogin.refreshToken}"
        } else {
            "null"
        }
    }
}