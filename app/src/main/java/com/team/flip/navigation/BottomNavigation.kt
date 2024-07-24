package com.team.flip.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.ScreenItem
import com.team.presentation.addflip.view.AddFlipScreen
import com.team.presentation.flip.view.FlipScreen
import com.team.presentation.home.view.HomeScreen
import com.team.presentation.home.viewmodel.HomeViewModel
import com.team.presentation.profile.view.ProfileScreen

/**
 * Flip 의 하단 탐색 바 네비게이션
 */
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    bottomNavController: NavHostController,
    onSettingClick: () -> Unit,
    deleteToken: () -> Unit,
) {

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(FlipTheme.colors.white),
        navController = bottomNavController,
        startDestination = ScreenItem.HOME.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None},
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = ScreenItem.HOME.name) {

            val homeViewModel: HomeViewModel = hiltViewModel()
            val categoryState by homeViewModel.categoriesState.collectAsStateWithLifecycle()
            val postState by homeViewModel.postState.collectAsStateWithLifecycle()

            val lifecycleOwner = LocalLifecycleOwner.current
            LaunchedEffect(lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    homeViewModel.fetchCategories()
                }
            }

            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlipTheme.colors.white),
                categoryState = categoryState,
                postState = postState,
                flipCardUiEvent = homeViewModel::onFlipCardEvent,
                homeUiEvent = homeViewModel::onHomeUiEvent,
                onSettingClick = onSettingClick
            )
        }

        composable(route = ScreenItem.FLIP.name) {
            FlipScreen()
        }

        composable(
            route = ScreenItem.ADD_FLIP.name,
            enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Bottom) },
            exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Bottom) },
            popEnterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Bottom) },
            popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Bottom) }
        ) {
            AddFlipScreen()
        }

        composable(route = ScreenItem.PROFILE.name) {
            ProfileScreen(
                deleteToken = deleteToken
            )
        }
    }
}