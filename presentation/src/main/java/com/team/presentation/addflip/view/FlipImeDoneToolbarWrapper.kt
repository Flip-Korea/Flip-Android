package com.team.presentation.addflip.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.util.composable.keyboardVisibleState

/**
 * 키보드 툴바 Wrapper 이며, 기본 적으로 오른쪽 끝에 텍스트 버튼이 하나 있다.
 *
 * 더 다양한 동작 필요 시 직접 수정 하거나 확장 해야 한다.
 *
 * @see onDone 완료 버튼 클릭 시
 * @see content 키보드 툴바를 사용할 컴포저블
 */
@Composable
fun FlipImeDoneToolbarWrapper(
    onDone: () -> Unit,
    content: @Composable () -> Unit,
) {

    val isImeVisible by keyboardVisibleState()

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        // 키보드 툴바
        AnimatedVisibility(
            visible = isImeVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding(),
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(200))
        ) {
            Column {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = FlipTheme.colors.gray2
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickableSingleWithoutRipple { onDone() },
                        text = stringResource(id = R.string.keyboard_toolbar_btn_done),
                        style = FlipTheme.typography.headline3,
                        color = FlipTheme.colors.point
                    )
                }
            }
        }
    }
}