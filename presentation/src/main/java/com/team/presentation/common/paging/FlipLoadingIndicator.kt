package com.team.presentation.common.paging

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipLoadingIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 3.dp,
    color: Color = FlipTheme.colors.gray3,
    size: Dp = 50.dp
) {
    CircularProgressIndicator(
        modifier = Modifier.size(size),
        strokeWidth = strokeWidth,
        strokeCap = StrokeCap.Round,
        trackColor = Color.Transparent,
        color = color
    )
}