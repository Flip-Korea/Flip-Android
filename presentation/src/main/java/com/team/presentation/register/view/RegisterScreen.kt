package com.team.presentation.register.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.topbar.FlipTopBar
import com.team.designsystem.theme.FlipAppTheme
import com.team.presentation.common.util.CommonPaddingValues

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    // a. 서비스 이용약관 동의 화면
    // b. 회원가입 1, 2, 3 단계
    // c. 가입 완료 화면

    var topBarTitle by rememberSaveable { mutableStateOf("") }
    var bottomBarTitle by rememberSaveable { mutableStateOf("") }

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
            bottomBarTitle = bottomBarTitle,
        ) {
            // Pager 혹은 Navigation 으로 구성된 회원가입 단계 페이지들
        }
    }
}

@Composable
fun RegisterScreenFrame(
    modifier: Modifier = Modifier,
    bottomBarTitle: String,
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
            onClick = { },
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    title: String,
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

@Preview
@Composable
private fun RegisterScreenPreview() {
    FlipAppTheme {
        RegisterScreen()
    }
}
