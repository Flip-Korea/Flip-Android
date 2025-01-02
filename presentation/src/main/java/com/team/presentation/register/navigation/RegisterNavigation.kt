package com.team.presentation.register.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.register.RegisterScreenPage.Companion.RegisterScreenPages
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.view.RegisterScreenTemp
import com.team.presentation.register.viewmodel.RegisterViewModel
import com.team.presentation.util.sharedViewModel

@Deprecated("다른 네비게이션 사용 예정")
fun NavGraphBuilder.registerNavigation(navController: NavHostController) {
    navigation(
        route = NavigationItem.RegisterNav.name,
        startDestination = ScreenItem.Register.name,
    ) {
        @OptIn(ExperimentalFoundationApi::class)
        composable(ScreenItem.Register.name) {
            val registerViewModel = it.sharedViewModel<RegisterViewModel>(navController)
            val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
            val pagerState = rememberPagerState { RegisterScreenPages.size }
            val scope = rememberCoroutineScope()

            ObserveAsEvents(flow = registerViewModel.effect) { effect ->
                when (effect) {
                    RegisterContract.UiEffect.BackPress -> {
                        navController.popBackStack()
                    }

                    is RegisterContract.UiEffect.NavigateTo -> {
                    }
                }
            }

            RegisterScreenTemp(
                uiState = uiState,
                pagerState = pagerState,
                onUiEvent = registerViewModel::processEvent,
            )
        }
    }
}
