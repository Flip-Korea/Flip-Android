package com.team.flip.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.designsystem.theme.FlipTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.flip.R
import com.team.flip.navigation.bottom_nav.FlipBottomNavigationBar
import com.team.presentation.NavigationItem
import com.team.presentation.ScreenItem
import com.team.presentation.editcategories.view.EditMyCategoriesScreen
import com.team.presentation.editcategories.viewmodel.EditMyCategoriesViewModel

/**
 * Flip 의 메인 네비게이션
 */
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    bottomNavController: NavHostController,
    deleteToken: () -> Unit,
) {

    NavHost(
        modifier = modifier,
        navController = mainNavController,
        startDestination = NavigationItem.BOTTOM_NAV.name,
        enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Left) },
        exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Left) },
        popEnterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Left) },
        popExitTransition = {FlipTransitionObject.exitTransition(FlipTransitionDirection.Left) }
    ) {
        composable(NavigationItem.BOTTOM_NAV.name) {

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { FlipBottomNavigationBar(navController = bottomNavController) }
            ) { paddingValues ->

                BottomNavigation(
                    //TODO 아직 못정함, 정하고 변경 예정
                    // modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                    modifier = Modifier.padding(paddingValues),
                    bottomNavController = bottomNavController,
                    onSettingClick = {
                        mainNavController.navigate(ScreenItem.EDIT_MY_CATEGORIES.name)
                    },
                    deleteToken = deleteToken,
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

            val context = LocalContext.current
            val editMyCategoriesViewModel: EditMyCategoriesViewModel = hiltViewModel()
            val myCategoriesState by editMyCategoriesViewModel.myCategoriesState.collectAsStateWithLifecycle()
            val nicknameState by editMyCategoriesViewModel.nicknameState.collectAsStateWithLifecycle()
            val speechBubbleState by editMyCategoriesViewModel.speechBubbleState.collectAsStateWithLifecycle()
            val myCategoriesUpdateState by editMyCategoriesViewModel.myCategoriesUpdateState.collectAsStateWithLifecycle()

            val nickname = nicknameState.ifEmpty {
                context.resources.getString(R.string.placeholder_nickname)
            }

            LaunchedEffect(myCategoriesUpdateState) {
                if (myCategoriesUpdateState.success) {
                    mainNavController.popBackStack()
                }
            }

            EditMyCategoriesScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlipTheme.colors.white)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(bottom = 26.dp),
                nickname = nickname,
                myCategoriesState = myCategoriesState,
                speechBubbleState = speechBubbleState,
                myCategoriesUpdateState = myCategoriesUpdateState,
                updateMyCategories = editMyCategoriesViewModel::updateMyCategories,
                uiEvent = editMyCategoriesViewModel::onUiEvent,
                onBackPress = {
                    //TODO 실행 시 HomeScreen 으로 돌아가는데 이 부분에서 딜레이 발생, 해결 필요
                    // (HomeScreen, HomeViewModel 체크)
                    mainNavController.popBackStack()
                }
            )
        }
    }
}