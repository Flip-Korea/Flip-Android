package com.team.presentation.register.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.register.view.RegisterScreen
import com.team.presentation.register.viewmodel.RegisterViewModel

fun NavGraphBuilder.registerNavigation(navController: NavHostController) {
    navigation(
        route = NavigationItem.REGISTER_NAV.name,
        startDestination = ScreenItem.REGISTER.name,
    ) {
        composable(ScreenItem.REGISTER.name) {
            val registerViewModel: RegisterViewModel = viewModel()
            val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()

            RegisterScreen(
                uiState = uiState,
                onUiEvent = registerViewModel::processEvent,
            )
        }
    }
}
