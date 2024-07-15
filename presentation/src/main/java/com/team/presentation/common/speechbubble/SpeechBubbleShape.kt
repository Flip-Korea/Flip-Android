package com.team.presentation.common.speechbubble

import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * 말풍선 모양
 * @param tipWidth 팁 사이즈의 가로 길이
 * @param tipHeight 팁 사이즈의 세로 길이
 * @param tipCornerWidth 팁의 둥근 부분 가로 길이
 * @param tipCornerHeight 팁의 둥근 부분 세로 길이
 * @param cornerRadius 말풍선의 CornerRadius
 * @param leftSpacePx 팁이 왼쪽(0)으로 부터 떨어진 길이
 */
class SpeechBubbleShape(
    private val tipWidth: Dp = 19.dp,
    private val tipHeight: Dp = 12.5.dp,
    private val tipCornerWidth: Dp =  3.dp,
    private val tipCornerHeight: Dp = 1.dp,
    private val cornerRadius: Dp = 5.dp,
    private val leftSpacePx: Dp = 36.dp
): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val tipWidthPx = with(density) { tipWidth.toPx() }
        val tipHeightPx = with(density) { tipHeight.toPx() }
        val tipCornerWidthPx = with(density) { tipCornerWidth.toPx() }
        val tipCornerHeightPx = with(density) { tipCornerHeight.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val leftSpacePx = with(density) { leftSpacePx.toPx() }

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    right = size.width,
                    bottom = size.height,
                    top = tipHeightPx,
                    radiusX = cornerRadiusPx,
                    radiusY = cornerRadiusPx
                )
            )
            moveTo(
                x = leftSpacePx,
                y = tipHeightPx
            )
            lineTo(
                x = (leftSpacePx + (tipWidthPx / 2)) - tipCornerWidthPx / 2,
                y = tipCornerHeightPx
            )
            quadraticBezierTo(
                x1 = leftSpacePx + tipWidthPx / 2,
                y1 = 0f,
                x2 = (leftSpacePx + (tipWidthPx / 2)) + tipCornerWidthPx / 2,
                y2 = tipCornerHeightPx
            )
            lineTo(
                x = leftSpacePx + tipWidthPx,
                y = tipHeightPx
            )
            close()
        }

        return Outline.Generic(path)
    }
}