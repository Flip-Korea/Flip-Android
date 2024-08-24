package com.team.designsystem.component.utils

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Figma의 Drop Shadow를 구현하는 Modifier 확장 함수 **/
fun Modifier.dropShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    spreadRadius: Dp = 0.dp,
    modifier: Modifier = Modifier
) = then(
    modifier.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spreadRadius.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = size.width + spreadPixel
            val bottomPixel = size.height + spreadPixel

            frameworkPaint.color = color.toArgb()

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = BlurMaskFilter(
                    blurRadius.toPx(),
                    BlurMaskFilter.Blur.NORMAL
                )
            }

            canvas.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint = paint
            )
        }
    }
)

fun Modifier.dropShadow1() = this
    .dropShadow(
        color = Color(0xFF000000).copy(0.1f),
        offsetY = 4.dp,
        blurRadius = 12.dp
    )

fun Modifier.dropShadow2() = this
    .dropShadow(
        color = Color(0xFF636363).copy(0.2f),
        offsetY = 2.dp,
        blurRadius = 8.dp
    )

fun Modifier.dropShadow3() = this
    .dropShadow(
        color = Color(0xFF000000).copy(0.35f),
        offsetY = 5.dp,
        blurRadius = 15.dp
    )