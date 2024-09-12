package com.team.flip.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.flip.navigation.bottom_nav.FlipBottomNavigationBar
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.editcategories.EditCategoriesRoute

/**
 * Flip 의 메인 네비게이션
 */
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    deleteToken: () -> Unit,
) {

    NavHost(
        modifier = modifier,
        navController = mainNavController,
        startDestination = NavigationItem.BOTTOM_NAV.name,
        enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Left) },
        popEnterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Left) },
        exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Left) },
        popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Left) }
    ) {
        composable(NavigationItem.BOTTOM_NAV.name) {

            val bottomNavController = rememberNavController()

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                bottomBar = { FlipBottomNavigationBar(navController = bottomNavController) }
            ) { innerPadding ->

                BottomNavigation(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FlipTheme.colors.white) // Background Color of padding
                        .padding(innerPadding),
                    bottomNavController = bottomNavController,
                    onSettingClick = {
                        mainNavController.navigate(ScreenItem.EDIT_MY_CATEGORIES.name)
                    },
                    deleteToken = deleteToken,
                    innerPadding = innerPadding
                )
            }
        }

        //TODO 어차피 프로필 정보중에 이름만 받아오는데 인자 값으로 받아오면 안되나? argument 기능 써가지고...
        composable(
            route = ScreenItem.EDIT_MY_CATEGORIES.name,
            enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Right) },
            exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Right) },
            popEnterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Right) },
            popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Right) },
        ) {

            EditCategoriesRoute(
                popBackStack = { mainNavController.popBackStack() }
            )
        }
    }
}