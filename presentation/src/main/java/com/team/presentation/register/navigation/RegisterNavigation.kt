package com.team.presentation.register.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.common.snackbar.ObserveAsEvents
import com.team.presentation.register.RegisterScreenPage.Companion.REGISTER_SCREEN_PAGES
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.view.RegisterScreenTemp
import com.team.presentation.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.registerNavigation(navController: NavHostController) {
    navigation(
        route = NavigationItem.REGISTER_NAV.name,
        startDestination = ScreenItem.REGISTER.name,
    ) {
        @OptIn(ExperimentalFoundationApi::class)
        composable(ScreenItem.REGISTER.name) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
            val pagerState = rememberPagerState { REGISTER_SCREEN_PAGES.size }
            val scope = rememberCoroutineScope()

            ObserveAsEvents(flow = registerViewModel.effect) { effect ->
                when (effect) {
                    RegisterContract.UiEffect.BackPress -> {
                        navController.popBackStack()
                    }

                    RegisterContract.UiEffect.GoToNextPage -> {
                        pagerState.animateScrollToPage(scope, 1)
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

@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.animateScrollToPage(
    scope: CoroutineScope,
    step: Int,
) {
    scope.launch {
        this@animateScrollToPage.animateScrollToPage(
            this@animateScrollToPage.currentPage + step,
        )
    }
}
