package com.team.flip

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.login.navigation.LoginNavigation
import com.team.presentation.login.util.GoogleAuthManager
import com.team.presentation.login.util.KakaoAuthManager
import dagger.hilt.android.AndroidEntryPoint

/** Flip 로그인 및 회원가입을 위한 액티비티**/
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val googleAuthManager by lazy {
        GoogleAuthManager(
            context = applicationContext,
            credentialManager = CredentialManager.create(applicationContext)
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

            FlipAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FlipTheme.colors.white)
                ) {
                    LoginNavigation(
                        navController = navController,
                        googleAuthManager, kakaoAuthManager,
                        onNavigateMain = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}