package com.team.presentation.register.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.register.view.RegisterScreen

fun NavGraphBuilder.registerNavigation(
    navController: NavHostController,
) {

    navigation(
        route = NavigationItem.REGISTER_NAV.name,
        startDestination = ScreenItem.REGISTER.name
    ) {
        composable(ScreenItem.REGISTER.name) {
            RegisterScreen()
        }
    }
}