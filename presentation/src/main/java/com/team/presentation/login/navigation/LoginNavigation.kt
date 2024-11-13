package com.team.presentation.login.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.designsystem.theme.FlipTheme
import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.login.util.GoogleAuthManager
import com.team.presentation.login.util.KakaoAuthManager
import com.team.presentation.login.view.LoginScreen
import com.team.presentation.login.viewmodel.LoginViewModel
import com.team.presentation.register.navigation.registerNavigation

@Composable
fun LoginNavigation(
    navController: NavHostController,
    googleAuthManager: GoogleAuthManager,
    kakaoAuthManager: KakaoAuthManager,
    onNavigateMain: () -> Unit
) {

    NavHost(
        modifier = Modifier.fillMaxSize().background(FlipTheme.colors.white),
        navController = navController,
        startDestination = ScreenItem.LOGIN.name
    ) {
        composable(ScreenItem.LOGIN.name) {

            val loginViewModel: LoginViewModel = hiltViewModel()
            val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

            LaunchedEffect(loginState) {
                if (loginState.error == null && loginState.accountExists != null) {
                    if (loginState.accountExists!!) {
                        onNavigateMain()
                    }
                    if (!loginState.accountExists!!) {
                        navController.navigate(NavigationItem.REGISTER_NAV.name)
                    }
                }
            }

            LoginScreen(
                loginState = loginState,
                onLoginClick = { socialLoginPlatform ->
                    val authManager = when(socialLoginPlatform) {
                        SocialLoginPlatform.GOOGLE -> { googleAuthManager }
                        SocialLoginPlatform.KAKAO -> { kakaoAuthManager }
                    }
                    loginViewModel.login(socialLoginPlatform, authManager)
                }
            )
        }

        registerNavigation(navController)
    }
}