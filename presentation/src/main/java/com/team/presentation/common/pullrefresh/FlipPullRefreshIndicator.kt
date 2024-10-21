package com.team.presentation.common.pullrefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipTheme

/**
 * [FlipPullToRefreshWrapper]에서 사용되는 인디케이터
 *
 * ([CircularProgressIndicator]를 커스텀)
 *
 * @param progress 인디케이터에 적용할 진행률
 * @param isLoading 인디케이터의 로딩 여부
 * @param strokeWidth 인디케이터의 두께
 * @param color 인디케이터의 색상
 * @param size 인디케이터의 크기
 */
@Composable
fun FlipPullRefreshIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    isLoading: Boolean,
    strokeWidth: Dp = 3.dp,
    color: Color = FlipTheme.colors.gray3,
    size: Dp = 50.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                strokeWidth = strokeWidth,
                strokeCap = StrokeCap.Round,
                trackColor = Color.Transparent,
                color = color
            )
        } else {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(size),
                strokeWidth = strokeWidth,
                strokeCap = StrokeCap.Round,
                trackColor = Color.Transparent,
                color = color
            )
        }
    }
}