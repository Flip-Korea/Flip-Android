package com.team.presentation.register.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.common.image.FlipImageFactory
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.RegisterContract

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: RegisterContract.UiState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
    onBackPress: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentPage = navBackStackEntry?.destination?.route?.toRegisterScreenPage()

    RegisterScreenFrame(
        currentPage = currentPage,
        topBarTitle = "",
        onBackPress = onBackPress,
    ) {
        RegisterNavigation(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            uiState = uiState,
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
fun RegisterNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: RegisterContract.UiState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RegisterScreenPage.TermsOfService.route,
        enterTransition = { FlipTransitionObject.enterTransition(FlipTransitionDirection.Right) },
        popEnterTransition = {
            FlipTransitionObject.enterTransition(FlipTransitionDirection.Left)
        },
        exitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Left) },
        popExitTransition = { FlipTransitionObject.exitTransition(FlipTransitionDirection.Right) },
    ) {
        composable(route = RegisterScreenPage.TermsOfService.route) {
            TermsOfServiceScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(ContentPaddingValues),
                inputAgreementsState = uiState.inputAgreementsState,
                onUiEvent = onUiEvent,
            )
        }

        composable(route = RegisterScreenPage.InputNickname.route) {
            InputNicknameScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(ContentPaddingValues),
                currentStep = 1,
                totalSteps = 3,
                inputNicknameState = uiState.inputNicknameState,
                onUiEvent = onUiEvent,
            )
        }

        composable(route = RegisterScreenPage.InputID.route) {
            InputIdScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(ContentPaddingValues),
                currentStep = 2,
                totalSteps = 3,
                inputIdState = uiState.inputIdState,
                onUiEvent = onUiEvent,
            )
        }

        composable(route = RegisterScreenPage.InputImage.route) {
            InputImageScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(ContentPaddingValues),
                currentStep = 3,
                totalSteps = 3,
                inputImageState = uiState.inputImageState,
                openPhotoCropper = { navController.navigate(SelectImageRoute) },
                onUiEvent = onUiEvent,
            )
        }

        composable(route = SelectImageRoute) {
            ImageCropScreen(
                modifier = Modifier.fillMaxSize(),
                onCancel = { navController.popBackStack() },
                onCrop = { imageBitmap ->
                    val image = FlipImageFactory(imageBitmap).create()
                    onUiEvent(RegisterContract.UiEvent.OnImageChanged(image))
                    navController.popBackStack()
                },
            )
        }

        composable(route = RegisterScreenPage.Finish.route) {
            RegisterFinishScreen()
        }
    }
}

private val SCREEN_BOTTOM_PADDING = 26.dp
private val ContentTopPadding = 24.dp
private val ContentHorizontalPadding = 16.dp
private val ContentPaddingValues =
    PaddingValues(
        top = ContentTopPadding,
        start = ContentHorizontalPadding,
        end = ContentHorizontalPadding,
        bottom = SCREEN_BOTTOM_PADDING,
    )
private const val SelectImageRoute = "select_image_route"

private fun String?.toRegisterScreenPage(): RegisterScreenPage? =
    when (this) {
        RegisterScreenPage.TermsOfService.route -> RegisterScreenPage.TermsOfService
        RegisterScreenPage.InputNickname.route -> RegisterScreenPage.InputNickname
        RegisterScreenPage.InputID.route -> RegisterScreenPage.InputID
        RegisterScreenPage.InputImage.route -> RegisterScreenPage.InputImage
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
                RegisterContract.UiState(),
            onUiEvent = { },
            onBackPress = { },
        )
    }
}

private val AgreementItemsTestData = AgreementItem.allItems.associateWith { false }
private val AgreementItemChecksTestData = List(AgreementItemsTestData.size) { false }
