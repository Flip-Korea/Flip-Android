package com.team.flip.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.ScreenItem
import com.team.presentation.addflip.view.AddFlipScreen
import com.team.presentation.editcategories.view.EditMyCategoriesScreen
import com.team.presentation.editcategories.viewmodel.EditMyCategoriesViewModel
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
    deleteToken: () -> Unit,
) {

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(FlipTheme.colors.white),
        navController = navController,
        startDestination = ScreenItem.HOME.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None},
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        val currentRoute = navController.currentDestination?.route

        composable(
            route = ScreenItem.HOME.name,
            enterTransition = {
                  if (currentRoute == ScreenItem.EDIT_MY_CATEGORIES.name) {
                      slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                  } else {
                      EnterTransition.None
                  }
            },
            exitTransition = {
                if (currentRoute == ScreenItem.EDIT_MY_CATEGORIES.name) {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (currentRoute == ScreenItem.EDIT_MY_CATEGORIES.name) {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = {
                if (currentRoute == ScreenItem.EDIT_MY_CATEGORIES.name) {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                } else {
                    ExitTransition.None
                }
            }
        ) {

            val homeViewModel: HomeViewModel = hiltViewModel()
            val categoryState by homeViewModel.categoriesState.collectAsStateWithLifecycle()
            val postState by homeViewModel.postState.collectAsStateWithLifecycle()

            HomeScreen(
                categoryState = categoryState,
                postState = postState,
                flipCardUiEvent = homeViewModel::onFlipCardEvent,
                homeUiEvent = homeViewModel::onHomeUiEvent,
                onSettingClick = {
                    navController.navigate(ScreenItem.EDIT_MY_CATEGORIES.name)
                }
            )
        }

        composable(route = ScreenItem.FLIP.name) {
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

        composable(route = ScreenItem.PROFILE.name) {
            ProfileScreen(
                deleteToken = deleteToken
            )
        }

        /** 어차피 프로필 정보중에 이름만 받아오는데 인자 값으로 받아오면 안되나? argument 기능 써가지고... */
        composable(
            route = ScreenItem.EDIT_MY_CATEGORIES.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) {

            val editMyCategoriesViewModel: EditMyCategoriesViewModel = hiltViewModel()
            val myCategoriesState by editMyCategoriesViewModel.myCategoriesState.collectAsStateWithLifecycle()
            val myProfileState by editMyCategoriesViewModel.myProfileState.collectAsStateWithLifecycle()

            EditMyCategoriesScreen(
                modifier = Modifier.fillMaxSize().background(Color.White),
                nickname = myProfileState.myProfile?.nickname ?: "플리퍼",
                myCategoriesState = myCategoriesState,
                onBackPress = { navController.popBackStack() }
            )
        }
    }
}