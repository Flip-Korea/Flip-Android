package com.team.designsystem.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.modal.FlipModalWrapper
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

/**
 * Flip에서 사용되는 로딩 뷰
 *
 * ([FlipModalWrapper] 사용)
 *
 * @param isLoading 로딩 여부
 * @param text 로딩 문구
 */
@Composable
fun FlipLoadingScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    text: String,
) {

    FlipModalWrapper(isOpen = isLoading, onDismissRequest = { }, animated = false) {
        Box(
            modifier = modifier
                .clip(FlipTheme.shapes.roundedCornerSmall)
                .sizeIn(minWidth = 160.dp, minHeight = 160.dp)
                .background(FlipTheme.colors.main.copy(0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = FlipTheme.colors.white,
                    strokeWidth = 3.dp,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = text,
                    style = FlipTheme.typography.body5,
                    color = FlipTheme.colors.white
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipLoadingScreenPreview() {

    var count by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    FlipAppTheme {
        FlipLoadingScreen(
            isLoading = isLoading,
            text = "임시 저장 중",
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                40.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(text = "$count")
            Button(onClick = { count++ }) {
                Text(text = "Click!")
            }
            Button(onClick = { isLoading = true }) {
                Text(text = "Loading Start")
            }
        }
    }
}