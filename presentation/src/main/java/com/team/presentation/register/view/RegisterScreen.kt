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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.team.presentation.register.findByOrder
import com.team.presentation.register.state.RegisterContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** 회원가입 화면 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterContract.UiState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    // a. 서비스 이용약관 동의 화면
    // b. 회원가입 1, 2, 3 단계
    // c. 가입 완료 화면

    val scope = rememberCoroutineScope()
    var topBarTitle by rememberSaveable { mutableStateOf("") }
    val pagerState = rememberPagerState { REGISTER_SCREEN_PAGES.size }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    when (uiState) {
        is RegisterContract.UiState.Error -> TODO()
        RegisterContract.UiState.Loading -> TODO()
        is RegisterContract.UiState.Success -> {
            Scaffold(
                modifier = modifier,
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
                            id = REGISTER_SCREEN_PAGES[currentPage].buttonTitle,
                        ),
                    bottomBarEnabled = uiState.agreementItemChecks.all { it },
                    bottomBarClick = { pagerState.animateScrollToPage(scope, 1) },
                ) {
                    PagerScreens(
                        pagerState = pagerState,
                        agreementItems = uiState.agreementItems,
                        agreementItemChecks = uiState.agreementItemChecks,
                        onUiEvent = onUiEvent,
                    )
                }
            }
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
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = false,
    ) { page ->
        when (REGISTER_SCREEN_PAGES.findByOrder(page)) {
            RegisterScreenPage.TERMS_OF_SERVICE ->
                TermsOfServiceScreen(
                    modifier = Modifier.fillMaxSize(),
                    agreementItems = agreementItems,
                    agreementItemChecks = agreementItemChecks,
                    onUiEvent = onUiEvent,
                )

            RegisterScreenPage.INPUT_NAME -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "INPUT_NAME")
                }
            }

            RegisterScreenPage.INPUT_ID -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "INPUT_ID")
                }
            }

            RegisterScreenPage.INPUT_PHOTO -> {
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
fun RegisterScreenFrame(
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
fun BottomBar(
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
                    start = HORIZONTAL_PADDING,
                    end = HORIZONTAL_PADDING,
                    bottom = BOTTOM_PADDING,
                ),
        text = title,
        onClick = onClick,
        enabled = enabled,
    )
}

@Composable
fun TopBar(
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

private val HORIZONTAL_PADDING = 16.dp
private val BOTTOM_PADDING = 26.dp
private val REGISTER_SCREEN_PAGES =
    listOf(
        RegisterScreenPage.TERMS_OF_SERVICE,
        RegisterScreenPage.INPUT_NAME,
        RegisterScreenPage.INPUT_ID,
        RegisterScreenPage.INPUT_PHOTO,
    )

@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.animateScrollToPage(
    scope: CoroutineScope,
    step: Int,
) {
    scope.launch {
        this@animateScrollToPage.animateScrollToPage(
            this@animateScrollToPage.currentPage + step,
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    FlipAppTheme {
        RegisterScreen(
            uiState = UiStateTestData,
            onUiEvent = { },
        )
    }
}

private val UiStateTestData =
    RegisterContract.UiState.Success(
        agreementItems = AgreementItem.allItems,
        agreementItemChecks = List(AgreementItem.allItems.size) { true },
    )
