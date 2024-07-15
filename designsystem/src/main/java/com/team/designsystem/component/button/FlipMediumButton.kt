package com.team.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import kotlinx.coroutines.delay

/**
 * 중간 사이즈 Button
 *
 * @param text 버튼 텍스트
 * @param enabled 버튼 활성화 여부
 * @param isLoading 로딩 여부 (true -> 로딩 애니메이션 재생)
 * @param onClick 버튼 클릭 시
 */
@Composable
fun FlipMediumButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    Button(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier
            .height(48.dp)
            .background(if (!enabled) FlipTheme.colors.gray4 else Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            containerColor = FlipTheme.colors.main,
            contentColor = FlipTheme.colors.white
        ),
        enabled = enabled,
        shape = FlipTheme.shapes.roundedCornerSmall,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(34.dp),
                color = FlipTheme.colors.white,
                strokeWidth = 3.dp,
            )
        } else {
            Text(
                text = text,
                style = FlipTheme.typography.headline3,
                color = FlipTheme.colors.white
            )
        }
    }
}

@Preview(name = "enabled", showBackground = true)
@Composable
private fun FlipMediumButtonPreview() {

    var isClick by remember { mutableStateOf(false) }

    LaunchedEffect(isClick) {
        if (isClick) {
            delay(3000L)
            isClick = false
        }
    }

    FlipAppTheme {
        FlipMediumButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "sample",
            enabled = true,
            isLoading = isClick,
            onClick = { isClick = !isClick }
        )
    }
}

@Preview(name = "disabled", showBackground = true)
@Composable
private fun FlipMediumButton2Preview() {
    FlipAppTheme {
        FlipMediumButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "sample",
            enabled = false,
            isLoading = false,
            onClick = {}
        )
    }
}

@Preview(name = "loading", showBackground = true)
@Composable
private fun FlipMediumButton3Preview() {
    FlipAppTheme {
        FlipMediumButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "sample",
            enabled = true,
            isLoading = true,
            onClick = {}
        )
    }
}