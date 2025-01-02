package com.team.presentation.register.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.topbar.FlipTopBar
import com.team.designsystem.theme.FlipAppTheme
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.RegisterScreenPage.Companion.RegisterScreenPages
import com.team.presentation.register.findByOrder
import com.team.presentation.register.state.InputNameState
import com.team.presentation.register.state.RegisterContract

@Deprecated("임시 코드")
/** 회원가입 화면 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreenTemp(
    modifier: Modifier = Modifier,
    uiState: RegisterContract.UiState,
    pagerState: PagerState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    var topBarTitle by rememberSaveable { mutableStateOf("") }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = topBarTitle,
                onBackPress = { },
            )
        },
    ) { innerPadding ->
        RegisterScreenFrame(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            bottomBarTitle =
                stringResource(
                    id = RegisterScreenPages[currentPage].buttonTitle,
                ),
            bottomBarEnabled = uiState.agreementItemChecks.all { it },
            bottomBarClick = {
                // TODO: 현재 페이지 로직 실행 후 다음 단계로 넘어갈 수 있는지 확인
                val currentScreenPage =
                    RegisterScreenPages[
                        (currentPage + 1).coerceIn(
                            0,
                            RegisterScreenPage.entries.size,
                        ),
                    ]
                onUiEvent(RegisterContract.UiEvent.RequestToNextPage(currentScreenPage))
            },
        ) {
            PagerScreens(
                modifier = Modifier.fillMaxSize(),
                pagerState = pagerState,
                agreementItems = uiState.agreementItems,
                agreementItemChecks = uiState.agreementItemChecks,
                inputNameState = uiState.inputNameState,
                onUiEvent = onUiEvent,
            )
        }
    }
}

/** 회원가입 단계별 페이지 (Pager 사용) */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerScreens(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    agreementItems: List<AgreementItem>,
    agreementItemChecks: List<Boolean>,
    inputNameState: InputNameState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = false,
    ) { page ->
        when (RegisterScreenPages.findByOrder(page)) {
            RegisterScreenPage.TermsOfService ->
                TermsOfServiceScreen(
                    modifier = Modifier.fillMaxSize(),
                    agreementItems = agreementItems,
                    agreementItemChecks = agreementItemChecks,
                    onUiEvent = onUiEvent,
                )

            RegisterScreenPage.InputName -> {
                InputNameScreen(
                    modifier =
                        Modifier.fillMaxSize().padding(
                            top = SCREEN_TOP_PADDING,
                            start = SCREEN_HORIZONTAL_PADDING,
                            end = SCREEN_HORIZONTAL_PADDING,
                        ),
                    currentStep = 1,
                    totalSteps = RegisterScreenPages.size,
                    inputNameState = inputNameState,
                    onUiEvent = onUiEvent,
                )
            }

            RegisterScreenPage.InputID -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "INPUT_ID")
                }
            }

            RegisterScreenPage.InputPhoto -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "INPUT_PHOTO")
                }
            }

            null -> {
                // Never can't reached here... (maybe)
            }
        }
    }
}

@Composable
private fun RegisterScreenFrame(
    modifier: Modifier = Modifier,
    bottomBarTitle: String,
    bottomBarEnabled: Boolean,
    bottomBarClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    var bottomBarHeightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    Box(modifier = modifier) {
        Column(Modifier.verticalScroll(scrollState)) {
            content()
            Spacer(modifier = Modifier.height(bottomBarHeightDp))
        }
        BottomBar(
            modifier =
                Modifier
                    .onSizeChanged {
                        with(density) {
                            bottomBarHeightDp = it.height.toDp()
                        }
                    }.align(Alignment.BottomCenter),
            title = bottomBarTitle,
            enabled = bottomBarEnabled,
            onClick = bottomBarClick,
        )
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    FlipMediumButton(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    start = SCREEN_HORIZONTAL_PADDING,
                    end = SCREEN_HORIZONTAL_PADDING,
                    bottom = SCREEN_BOTTOM_PADDING,
                ),
        text = title,
        onClick = onClick,
        enabled = enabled,
    )
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPress: () -> Unit,
) {
    FlipTopBar(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(CommonPaddingValues.TopBarWithTouchTarget),
        title = title,
        onBackPress = onBackPress,
    )
}

private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val SCREEN_TOP_PADDING = 16.dp
private val SCREEN_BOTTOM_PADDING = 26.dp

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun RegisterScreenPreview() {
    FlipAppTheme {
        RegisterScreenTemp(
            pagerState = rememberPagerState { AgreementItem.allItems.size },
            uiState = UiStateTestData,
            onUiEvent = { },
        )
    }
}

private val UiStateTestData =
    RegisterContract.UiState(
        agreementItems = AgreementItem.allItems,
        agreementItemChecks = List(AgreementItem.allItems.size) { true },
    )
