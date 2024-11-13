package com.team.flip.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.addflip.AddFlipRoute
import com.team.presentation.tempflipbox.TempFlipBoxRoute

fun NavGraphBuilder.addFlipNavigation(
    currentRoute: String,
    popBackStack: () -> Unit,
    onNavigateToTempFlipBox: () -> Unit
) {
    navigation(
        route = NavigationItem.ADD_FLIP_NAV.name,
        startDestination = ScreenItem.ADD_FLIP.name
    ) {
        composable(
            route = ScreenItem.ADD_FLIP.name,
            enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Bottom) },
            exitTransition = { exitTransition(currentRoute) },
            popEnterTransition = { popEnterTransition(currentRoute) },
            popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Bottom) }
        ) {
            AddFlipRoute(
                popBackStack = popBackStack,
                onNavigateToTempFlipBox = onNavigateToTempFlipBox
            )
        }

        composable(
            route = ScreenItem.TEMP_FLIP_BOX.name,
            enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Right) },
            exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Right) },
            popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Right) }
        ) {
            TempFlipBoxRoute(
                onBackPress = popBackStack
            )
        }
    }
}

private val popEnterTransition: (String) -> EnterTransition = {
    when(it) {
        ScreenItem.ADD_FLIP.name -> FlipTransitionObject.enterTransition(FlipTransitionDirection.Left)
        else -> FlipTransitionObject.enterTransition(FlipTransitionDirection.Bottom)
    }
}

private val exitTransition: (String) -> ExitTransition = {
    when(it) {
        ScreenItem.TEMP_FLIP_BOX.name -> FlipTransitionObject.exitTransition(FlipTransitionDirection.Left)
        else -> FlipTransitionObject.exitTransition(FlipTransitionDirection.Bottom)
    }
}