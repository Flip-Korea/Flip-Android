package com.team.presentation.common.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipSmallButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.util.uitext.UiText

//TODO: 임시화면이고 디자인 팀에게 요청 필요
/**
 * 공통 에러 화면
 *
 * @param errorMessage [UiText] 타입의 에러
 * @param onRetry 재시도 버튼 클릭 시 수행할 작업
 */
@Composable
fun FlipErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: UiText,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(
            text = errorMessage.asString(),
            style = FlipTheme.typography.body6,
            color = FlipTheme.colors.statusRed,
        )
        FlipSmallButton(
            text = stringResource(id = R.string.common_error_screen_retry_btn),
            solid = true,
            onClick = onRetry
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    FlipAppTheme {
        FlipErrorScreen(
            errorMessage = UiText.DynamicString("에러스크린 입니당~"),
            onRetry = { }
        )
    }
}