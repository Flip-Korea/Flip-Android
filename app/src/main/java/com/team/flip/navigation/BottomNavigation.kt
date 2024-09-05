package com.team.flip.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.ScreenItem
import com.team.presentation.addflip.AddFlipRoute
import com.team.presentation.flip.view.FlipScreen
import com.team.presentation.home.HomeRoute
import com.team.presentation.profile.view.ProfileScreen

/**
 * Flip 의 하단 탐색 바 네비게이션
 */
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    bottomNavController: NavHostController,
    onSettingClick: () -> Unit,
    deleteToken: () -> Unit,
) {

    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(FlipTheme.colors.white),
        navController = bottomNavController,
        startDestination = ScreenItem.HOME.name,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { popEnterTransition(currentRoute ?: "") },
        exitTransition = { exitTransition(currentRoute ?: "") },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = ScreenItem.HOME.name) {
            HomeRoute(
                innerPadding = innerPadding,
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
            AddFlipRoute(
                popBackStack = { bottomNavController.popBackStack() }
            )
        }

        composable(route = ScreenItem.PROFILE.name) {
            ProfileScreen(
                deleteToken = deleteToken
            )
        }
    }
}

private val popEnterTransition: (String) -> EnterTransition = {
    when (it) {
        ScreenItem.ADD_FLIP.name -> FlipTransitionObject.scaleFadeIn
        else -> EnterTransition.None
    }
}

private val exitTransition: (String) -> ExitTransition = {
    when (it) {
        ScreenItem.ADD_FLIP.name -> FlipTransitionObject.scaleFadeOut
        else -> ExitTransition.None
    }
}