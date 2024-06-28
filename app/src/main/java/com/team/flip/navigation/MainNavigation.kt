package com.team.flip.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.ScreenItem
import com.team.presentation.add_flip.view.AddFlipScreen
import com.team.presentation.flip.view.FlipScreen
import com.team.presentation.home.view.HomeScreen
import com.team.presentation.home.viewmodel.HomeViewModel
import com.team.presentation.profile.view.ProfileScreen

/**
 * Flip의 메인 네비게이션
 */
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    deleteToken: () -> Unit
) {

    NavHost(
        modifier = modifier.fillMaxSize().background(FlipTheme.colors.white),
        navController = navController,
        startDestination = ScreenItem.HOME.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(ScreenItem.HOME.name) {

            val homeViewModel: HomeViewModel = viewModel()
            val categoryState by homeViewModel.categoriesState.collectAsStateWithLifecycle()
            val postState by homeViewModel.postState.collectAsStateWithLifecycle()

            HomeScreen(
                categoryState = categoryState,
                postState = postState,
                flipCardUiEvent = homeViewModel::onFlipCardEvent
            )
        }

        composable(ScreenItem.FLIP.name) {
            FlipScreen()
        }

        composable(
            route = ScreenItem.ADD_FLIP.name,
            enterTransition = { FlipTransitionObject.slideInVertically },
            exitTransition = { FlipTransitionObject.slideOutVertically },
            popEnterTransition = { FlipTransitionObject.slideInVertically },
            popExitTransition = { FlipTransitionObject.slideOutVertically }
        ) {
            AddFlipScreen()
        }

        composable(ScreenItem.PROFILE.name) {
            ProfileScreen(
                deleteToken = deleteToken
            )
        }
    }
}