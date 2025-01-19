package com.team.presentation.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.login.state.LoginState
import com.team.presentation.login.util.GoogleAuthManager
import com.team.presentation.login.util.KakaoAuthManager
import com.team.presentation.login.view.LoginScreen
import com.team.presentation.login.viewmodel.LoginViewModel

@Composable
fun LoginRoute(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    loginState: LoginState,
    googleAuthManager: GoogleAuthManager,
    kakaoAuthManager: KakaoAuthManager,
    onNavigateRegister: () -> Unit,
    onNavigateMain: () -> Unit,
) {
    LaunchedEffect(loginState) {
        if (loginState.accountExists != null) {
            if (loginState.accountExists) {
                onNavigateMain()
            }
            if (!loginState.accountExists) {
                onNavigateRegister()
            }
        }
    }

    LoginScreen(
        modifier = Modifier.fillMaxSize(),
        loginState = loginState,
        onLoginClick = { socialLoginPlatform ->
            val authManager =
                when (socialLoginPlatform) {
                    SocialLoginPlatform.Google -> {
                        googleAuthManager
                    }

                    SocialLoginPlatform.Kakao -> {
                        kakaoAuthManager
                    }
                }
            loginViewModel.login(socialLoginPlatform, authManager)
        },
    )
}
