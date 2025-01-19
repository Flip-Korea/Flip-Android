package com.team.presentation.login.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.login.LoginRoute
import com.team.presentation.login.util.GoogleAuthManager
import com.team.presentation.login.util.KakaoAuthManager
import com.team.presentation.login.viewmodel.LoginViewModel
import com.team.presentation.register.RegisterRoute

@Composable
fun LoginNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    googleAuthManager: GoogleAuthManager,
    kakaoAuthManager: KakaoAuthManager,
    onNavigateMain: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ScreenItem.Login.name,
    ) {
        composable(ScreenItem.Login.name) {
            val loginViewModel: LoginViewModel = hiltViewModel() // 주의
            val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

            val context = LocalContext.current

            ObserveAsEvents(flow = loginViewModel.errorEffect) { error ->
                Toast.makeText(context, error.asString(context), Toast.LENGTH_SHORT).show()
            }

            ObserveAsEvents(flow = loginViewModel.navigateEffect) { route ->
                navController.navigate(
                    "${route.name}/${loginState.loginInfo?.provider?.provider}/${loginState.loginInfo?.oauthId}",
                )
            }

            LoginRoute(
                navController = navController,
                loginViewModel = loginViewModel,
                loginState = loginState,
                googleAuthManager = googleAuthManager,
                kakaoAuthManager = kakaoAuthManager,
                onNavigateRegister = {
                    loginState.loginInfo?.let { login ->
                        navController.navigate(
                            "${NavigationItem.RegisterNav.name}/${login.provider}/${login.oauthId}",
                        )
                    }
                },
                onNavigateMain = onNavigateMain,
            )
        }

        composable(
            route = "${NavigationItem.RegisterNav.name}/{$CurrentLoginPlatform}/{$CurrentOAuthId}",
        ) { entry ->
            val currentLoginPlatform =
                entry.arguments?.getString(CurrentLoginPlatform)?.toSocialLoginPlatform()
            val currentOAuthId = entry.arguments?.getString(CurrentOAuthId)

            val nestedRegisterNavController = rememberNavController()

            RegisterRoute(
                navController = nestedRegisterNavController,
                currentLoginPlatform = currentLoginPlatform,
                currentOAuthId = currentOAuthId,
                onNavigateMain = onNavigateMain,
            )
        }
    }
}

private fun String.toSocialLoginPlatform(): SocialLoginPlatform? =
    when (this) {
        SocialLoginPlatform.Google.provider -> SocialLoginPlatform.Google
        SocialLoginPlatform.Kakao.provider -> SocialLoginPlatform.Kakao
        else -> null
    }

private const val CurrentLoginPlatform = "currentLoginPlatform"
private const val CurrentOAuthId = "currentOAuthId"
