package com.team.flip.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.presentation.ScreenItem
import com.team.presentation.home.view.HomeScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    deleteToken: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = ScreenItem.HOME.name
    ) {
        composable(ScreenItem.HOME.name) {

            HomeScreen(
                deleteToken = deleteToken
            )
        }
    }
}