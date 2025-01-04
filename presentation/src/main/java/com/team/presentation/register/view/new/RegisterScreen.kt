package com.team.presentation.register.view.new

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.view.InputIdScreen
import com.team.presentation.register.view.InputNameScreen
import com.team.presentation.register.view.TermsOfServiceScreen
import com.team.presentation.util.composable.copy

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: RegisterContract.UiState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
    onBackPress: () -> Unit,
) {
    RegisterScreenFrame(
        topBarTitle = "",
        bottomBarTitle = "동의",
        bottomBarEnabled = true,
        onBottomBarClick = {
            val currentPage =
                navController.currentDestination?.route?.toRegisterScreenPage()
            onUiEvent(RegisterContract.UiEvent.RequestToNextPage(currentPage))
        },
        onBackPress = onBackPress,
    ) { bottomBarHeightDp ->
        RegisterNavigation(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            bottomBarHeightDp = bottomBarHeightDp,
            uiState = uiState,
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
fun RegisterNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomBarHeightDp: Dp,
    uiState: RegisterContract.UiState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RegisterScreenPage.TermsOfService.route,
        enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Right) },
        popEnterTransition = {
            FlipTransitionObject.enterTransition(
                FlipTransitionDirection.Left,
            )
        },
        exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Left) },
        popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Right) },
    ) {
        composable(route = RegisterScreenPage.TermsOfService.route) {
            TermsOfServiceScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(ContentPaddingValues.copy(bottom = bottomBarHeightDp)),
                agreementItems = uiState.agreementItems,
                agreementItemChecks = uiState.agreementItemChecks,
                onUiEvent = onUiEvent,
            )
        }

        composable(route = RegisterScreenPage.InputName.route) {
            InputNameScreen(
                modifier = Modifier.fillMaxSize().padding(ContentPaddingValues),
                currentStep = 1,
                totalSteps = 3,
                inputNameState = uiState.inputNameState,
                onUiEvent = onUiEvent,
            )
        }

        composable(route = RegisterScreenPage.InputID.route) {
            InputIdScreen(
                modifier = Modifier.fillMaxSize().padding(ContentPaddingValues),
                currentStep = 2,
                totalSteps = 3,
                inputIdState = uiState.inputIdState,
                onUiEvent = onUiEvent,
            )
        }
    }
}

private val ContentTopPadding = 24.dp
private val ContentHorizontalPadding = 16.dp
private val ContentPaddingValues =
    PaddingValues(
        top = ContentTopPadding,
        start = ContentHorizontalPadding,
        end = ContentHorizontalPadding,
    )

private fun String?.toRegisterScreenPage(): RegisterScreenPage? =
    when (this) {
        RegisterScreenPage.TermsOfService.route -> RegisterScreenPage.TermsOfService
        RegisterScreenPage.InputName.route -> RegisterScreenPage.InputName
        RegisterScreenPage.InputID.route -> RegisterScreenPage.InputID
        RegisterScreenPage.InputPhoto.route -> RegisterScreenPage.InputPhoto
        else -> null
    }

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    val navController = rememberNavController()
    FlipAppTheme {
        RegisterScreen(
            navController = navController,
            uiState =
                RegisterContract.UiState(
                    agreementItems = AgreementItemsTestData,
                    agreementItemChecks = AgreementItemChecksTestData,
                ),
            onUiEvent = { },
            onBackPress = { },
        )
    }
}

private val AgreementItemsTestData = AgreementItem.allItems
private val AgreementItemChecksTestData = List(AgreementItemsTestData.size) { false }
