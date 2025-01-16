package com.team.presentation.register.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.topbar.FlipTopBar
import com.team.designsystem.theme.FlipAppTheme
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.register.RegisterScreenPage

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
 * @param currentPage 현재 회원가입 페이지
 * @param topBarTitle 탑바 타이틀
 * @param bottomBarTitle 바텀 버튼 타이틀
 * @param bottomBarEnabled 바텀 버튼 활성화 여부
 * @param isLoading 현재 회원가입 페이지 로딩 여부
 * @param onBottomBarClick 바텀 버튼 클릭 시
 * @param onBackPress 뒤로가기 클릭 시 (탑바 뒤로가기 버튼 클릭 시)
 * @param content 회원가입 컨텐츠 (UI)
 */
@Composable
fun RegisterScreenFrame(
    modifier: Modifier = Modifier,
    currentPage: RegisterScreenPage?,
    topBarTitle: String,
    onBackPress: () -> Unit,
    content: @Composable () -> Unit,
) {
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
            content()
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

/**
 * 회원가입 화면에서 공통적으로 사용하는 바텀 바(버튼)
 *
 * @param title 버튼 타이틀
 * @param enabled 버튼 활성화 여부
 * @param isLoading 기능 로딩 여부
 * @param onClick 버튼 클릭 시
 * @param option 버튼 하단에 위치할 Slot API
 */
@Composable
fun RegisterScreenBottomBar(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    option: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FlipMediumButton(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            onClick = onClick,
            enabled = enabled,
            isLoading = isLoading,
        )
        if (option != null) {
            option()
        }
    }
}

private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val SCREEN_BOTTOM_PADDING = 26.dp

@Preview
@Composable
private fun RegisterScreenFramePreview() {
    FlipAppTheme {
        RegisterScreenFrame(
            currentPage = RegisterScreenPage.TermsOfService,
            topBarTitle = "TopBarTitle",
            onBackPress = { },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(100) {
                    Text(modifier = Modifier.fillMaxWidth(), text = "$it")
                }
            }
        }
    }
}
