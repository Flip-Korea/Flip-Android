package com.team.presentation.register.view.new

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.topbar.FlipTopBar
import com.team.designsystem.theme.FlipAppTheme
import com.team.presentation.common.util.CommonPaddingValues

/**
 * 회원가입 화면에서 공통적으로 사용하는 프레임
 * #####
 * ##### 주의사항
 * [content] 의 파라미터인 `bottomBarHeightDp` 를 활용해서 하단에 바텀 버튼과 겹치지 않게 추가 설정 필요
 * ```
 * RegisterScreenFrame(
 *  // ...
 * ) { bottomHeightDp ->
 *      // ...
 *      // Register Contents
 *      // ...
 *      Spacer(modifier = Modifier.height(bottomHeightDp))
 * }
 * ```
 *
 * @param topBarTitle 탑바 타이틀
 * @param bottomBarTitle 바텀 버튼 타이틀
 * @param bottomBarEnabled 바텀 버튼 활성화 여부
 * @param onBottomBarClick 바텀 버튼 클릭 시
 * @param onBackPress 뒤로가기 클릭 시 (탑바 뒤로가기 버튼 클릭 시)
 * @param content 회원가입 컨텐츠 (UI)
 */
@Composable
fun RegisterScreenFrame(
    modifier: Modifier = Modifier,
    topBarTitle: String,
    bottomBarTitle: String,
    bottomBarEnabled: Boolean,
    onBottomBarClick: () -> Unit,
    onBackPress: () -> Unit,
    content: @Composable (bottomBarHeightDp: Dp) -> Unit,
) {
    var bottomBarHeightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                title = topBarTitle,
                onBackPress = onBackPress,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            content(bottomBarHeightDp)
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
                onClick = onBottomBarClick,
            )
        }
    }
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
                .height(44.dp)
                .padding(horizontal = CommonPaddingValues.TopBarHorizontalWithTouchTarget),
        title = title,
        onBackPress = onBackPress,
    )
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

private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val SCREEN_TOP_PADDING = 16.dp
private val SCREEN_BOTTOM_PADDING = 26.dp

@Preview
@Composable
private fun RegisterScreenFramePreview() {
    FlipAppTheme {
        RegisterScreenFrame(
            topBarTitle = "TopBarTitle",
            bottomBarTitle = "BottomBarTitle",
            bottomBarEnabled = true,
            onBottomBarClick = { },
            onBackPress = { },
        ) { bottomHeightDp ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(100) {
                    Text(modifier = Modifier.fillMaxWidth(), text = "$it")
                }
                item {
                    Spacer(modifier = Modifier.height(bottomHeightDp))
                }
            }
        }
    }
}
