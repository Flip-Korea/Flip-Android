package com.team.flip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.team.designsystem.theme.FlipAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipAppTheme {

                /** UI Test Section **/
//                val viewModel: LoginViewModel = hiltViewModel()
//                val loginState = viewModel.loginState.collectAsStateWithLifecycle()
//
//                LoginScreen(
//                    loginState = loginState.value,
//                    login = viewModel::login,
//                    getProfile = viewModel::getProfile,
//                    getTokenByDataStore = viewModel::getTokenByDataStore,
//                    logout = viewModel::logout,
//                    register = viewModel::register
//                )
            }
        }
    }
}