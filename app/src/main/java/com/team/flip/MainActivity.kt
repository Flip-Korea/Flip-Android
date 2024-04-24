package com.team.flip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.flip.designsystem.theme.FlipTheme
import com.team.flip.feature.ui_test.token_flow.LoginScreen
import com.team.flip.feature.ui_test.token_flow.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipTheme {

                /** UI Test Section **/

                val viewModel: LoginViewModel = hiltViewModel()
                val loginState = viewModel.loginState.collectAsStateWithLifecycle()

                LoginScreen(
                    loginState = loginState.value,
                    login = viewModel::login,
                    getProfile = viewModel::getProfile,
                    getTokenByDataStore = viewModel::getTokenByDataStore,
                    logout = viewModel::logout,
                    register = viewModel::register
                )
            }
        }
    }
}