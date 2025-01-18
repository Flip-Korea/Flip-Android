package com.team.flip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.theme.FlipAppTheme
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.login.navigation.LoginNavigation
import com.team.presentation.login.util.GoogleAuthManager
import com.team.presentation.login.util.KakaoAuthManager
import com.team.presentation.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Flip 로그인 및 회원가입을 위한 액티비티**/
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val googleAuthManager by lazy {
        GoogleAuthManager(
            context = applicationContext,
            credentialManager = CredentialManager.create(applicationContext),
        )
    }

    private val kakaoAuthManager by lazy {
        KakaoAuthManager(this@LoginActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        /** statusBarsPadding() & navigationBarsPadding() 사용하기 **/
        setContent {
            val navController = rememberNavController()

            val context = LocalContext.current
            val loginViewModel: LoginViewModel = hiltViewModel()
            val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

            ObserveAsEvents(flow = loginViewModel.errorEffect) { error ->
                Toast.makeText(context, error.asString(context), Toast.LENGTH_SHORT).show()
            }

            ObserveAsEvents(flow = loginViewModel.navigateEffect) { route ->
                navController.navigate(route.name)
            }

            FlipAppTheme {
                LoginNavigation(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    googleAuthManager = googleAuthManager,
                    kakaoAuthManager = kakaoAuthManager,
                    loginState = loginState,
                    login = loginViewModel::login,
                    onNavigateMain = {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                )
            }
        }
    }
}
