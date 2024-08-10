package com.team.flip.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.team.presentation.addflip.viewmodel.AddFlipViewModel
import com.team.presentation.common.bottomsheet.ReportAndBlockBottomSheet
import com.team.presentation.common.bottomsheet.ReportAndBlockUiEvent
import com.team.presentation.flip.view.FlipScreen
import com.team.presentation.home.view.HomeScreen
import com.team.presentation.home.viewmodel.HomeViewModel
import com.team.presentation.profile.view.ProfileScreen
import kotlinx.coroutines.launch

/**
 * Flip 의 하단 탐색 바 네비게이션
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    bottomNavController: NavHostController,
    onSettingClick: () -> Unit,
    deleteToken: () -> Unit,
    innerPadding: PaddingValues
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
            val reportState by homeViewModel.reportState.collectAsStateWithLifecycle()
            val blockState by homeViewModel.blockState.collectAsStateWithLifecycle()

            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var startFromReportView: Boolean? by remember { mutableStateOf(null) }
            var reportAndBlockProfileId by remember { mutableStateOf("") }
            var reportAndBlockPhotoUrl by remember { mutableStateOf("") }

            val coroutineScope = rememberCoroutineScope()

            val lifecycleOwner = LocalLifecycleOwner.current
            LaunchedEffect(lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    homeViewModel.fetchCategories()
                }
            }

            ReportAndBlockBottomSheet(
                startFromReportView = startFromReportView,
                sheetState = sheetState,
                reportState = reportState,
                blockState = blockState,
                profileId = reportAndBlockProfileId,
                photoUrl = reportAndBlockPhotoUrl,
                onDismissRequest = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            startFromReportView = null
                        }
                    }
                },
                onReport = { reportType -> homeViewModel.onReport() },
                onBlockClick = homeViewModel::onBlock,
            )

            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlipTheme.colors.white)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                categoryState = categoryState,
                postState = postState,
                flipCardUiEvent = homeViewModel::onFlipCardEvent,
                homeUiEvent = homeViewModel::onHomeUiEvent,
                onSettingClick = onSettingClick,
                reportAndBlockUiEvent = { reportAndBlockUiEvent ->
                    when (reportAndBlockUiEvent) {
                        is ReportAndBlockUiEvent.OnReport -> {
                            reportAndBlockProfileId = reportAndBlockUiEvent.profileId
                            reportAndBlockPhotoUrl = reportAndBlockUiEvent.photoUrl
                            startFromReportView = true
                        }
                        is ReportAndBlockUiEvent.OnBlock -> {
                            reportAndBlockProfileId = reportAndBlockUiEvent.profileId
                            reportAndBlockPhotoUrl = reportAndBlockUiEvent.photoUrl
                            startFromReportView = false
                        }
                    }
                },
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

            val addFlipViewModel: AddFlipViewModel = hiltViewModel()
            val categoriesState by addFlipViewModel.categoriesState.collectAsStateWithLifecycle()
            val selectedCategory by addFlipViewModel.selectedCategory.collectAsStateWithLifecycle()
            val addPostState by addFlipViewModel.addPostState.collectAsStateWithLifecycle()

            LaunchedEffect(addPostState) {
                if (addPostState.postSave) {
                    bottomNavController.popBackStack()
                }
            }

            AddFlipScreen(
                categoriesState = categoriesState,
                addPostState = addPostState,
                selectedCategory = selectedCategory,
                onUiEvent = addFlipViewModel::onUiEvent,
                onBackPress = { bottomNavController.popBackStack() },
                resetErrorState = addFlipViewModel::resetErrorState
            )
        }

        composable(route = ScreenItem.PROFILE.name) {
            ProfileScreen(
                deleteToken = deleteToken
            )
        }
    }

}