package com.team.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.view.RegisterScreen
import com.team.presentation.register.viewmodel.RegisterViewModel

@Composable
fun RegisterRoute(
    navController: NavHostController,
    currentLoginPlatform: SocialLoginPlatform?,
    currentOAuthId: String?,
    onNavigateMain: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<RegisterViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(currentLoginPlatform, currentOAuthId) {
        viewModel.processEvent(
            RegisterContract.UiEvent.SetPreviousLogin(currentLoginPlatform, currentOAuthId),
        )
    }

    ObserveAsEvents(flow = viewModel.effect) { effect ->
        when (effect) {
            RegisterContract.UiEffect.BackPress -> {
                navController.popBackStack()
            }

            is RegisterContract.UiEffect.NavigateTo -> {
                navController.navigate(route = effect.destination.route)
            }

            RegisterContract.UiEffect.NavigateToMain -> {
                onNavigateMain()
            }

            is RegisterContract.UiEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

    RegisterScreen(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        uiState = uiState,
        onUiEvent = viewModel::processEvent,
        onBackPress = {
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            }
        },
    )
}
