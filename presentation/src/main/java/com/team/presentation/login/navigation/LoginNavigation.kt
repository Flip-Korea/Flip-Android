package com.team.presentation.login.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.login.state.LoginState
import com.team.presentation.login.util.AuthManager
import com.team.presentation.login.util.GoogleAuthManager
import com.team.presentation.login.util.KakaoAuthManager
import com.team.presentation.login.view.LoginScreen

@Composable
fun LoginNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    googleAuthManager: GoogleAuthManager,
    kakaoAuthManager: KakaoAuthManager,
    loginState: LoginState,
    login: (SocialLoginPlatform, AuthManager) -> Unit,
    onNavigateMain: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ScreenItem.Login.name,
    ) {
        composable(ScreenItem.Login.name) {
            LaunchedEffect(loginState) {
                if (loginState.accountExists != null) {
                    if (loginState.accountExists) {
                        onNavigateMain()
                    }
                    if (!loginState.accountExists) {
                        navController.navigate(NavigationItem.RegisterNav.name)
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
                    login(socialLoginPlatform, authManager)
                },
            )
        }

//        registerNavigation(navController)
    }
}
