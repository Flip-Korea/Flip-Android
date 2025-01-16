package com.team.presentation.register.view.new

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team.designsystem.component.button.FlipTextButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTransitionDirection
import com.team.designsystem.theme.FlipTransitionObject
import com.team.presentation.R
import com.team.presentation.common.image.FlipImageFactory
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.view.ImageCropScreen
import com.team.presentation.register.view.InputIdScreen
import com.team.presentation.register.view.InputImageScreen
import com.team.presentation.register.view.InputNicknameScreen
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentPage = navBackStackEntry?.destination?.route?.toRegisterScreenPage()

    RegisterScreenFrame(
        currentPage = currentPage,
        topBarTitle = "",
        bottomBarTitle =
            stringResource(currentPage?.buttonTitle ?: RegisterScreenPage.defaultButtonTitle),
        bottomBarEnabled = uiState.agreementItemChecks.all { it },
        isLoading = uiState.loading,
        onBottomBarClick = { onUiEvent(RegisterContract.UiEvent.RequestToNextPage(currentPage)) },
        onBackPress = onBackPress,
        option = {
            if (currentPage?.buttonTitle != null && currentPage == RegisterScreenPage.InputImage) {
                FlipTextButton(
                    text = stringResource(id = R.string.register_screen_input_image_btn_2),
                    onClick = {
                        // TODO: 다음에 할래요 클릭 시 -> 프로필 사진 없는 채로 회원가입 완료
                    },
                )
            }
        },
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
                        .padding(ContentPaddingValues.copy(bottom = bottomBarHeightDp)),
                agreementItems = uiState.agreementItems,
                agreementItemChecks = uiState.agreementItemChecks,
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
