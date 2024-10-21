package com.team.presentation.flip.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipLightColors

/**
 * Flip(Short Form) 화면 최하단에 위치한 페이지 진행률 바
 *
 * @param currentPage 현재 페이지
 * @param maxPage 최대 페이지 개수 (기획 상, 3개 고정)
 */
@Composable
fun FlipPagesProgressBar(
    currentPage: Int,
    maxPage: Int = 3
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .drawWithContent {
                val cornerRadius = 2.dp.toPx()
                val barSpace = 5.dp.toPx()
                val barWidth = (this.size.width - barSpace * (maxPage - 1)) / maxPage
                val barHeight = 2.dp.toPx()

                repeat(maxPage) { idx ->

                    val color = if (idx <= currentPage) {
                        FlipLightColors.gray7
                    } else { FlipLightColors.gray2 }

                    drawRoundRect(
                        color = color,
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        size = Size(
                            width = barWidth,
                            height = barHeight
                        ),
                        topLeft = Offset((barWidth + barSpace) * idx, 0f)
                    )
                }
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun PagesProgressBarPreview() {
    FlipPagesProgressBar(currentPage = 1)
}

@Preview(showBackground = true)
@Composable
private fun PagesProgressBarPreview2() {
    FlipPagesProgressBar(
        currentPage = 2,
        maxPage = 5
    )
}