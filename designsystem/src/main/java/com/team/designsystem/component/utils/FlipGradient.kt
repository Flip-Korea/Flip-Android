package com.team.designsystem.component.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Flip 카드 및 Flip 단일화면에서 사용 되는 그라데이션 (Modifier 확장함수 버전) */
fun Modifier.flipGradient(
    color: Color,
    shape: Shape = RoundedCornerShape(3.dp)
) = then(
    background(
        brush = Brush.verticalGradient(listOf(color, color.copy(0f))),
        shape = shape
    )
)

/** Flip 카드 및 Flip 단일화면에서 사용 되는 그라데이션 (DrawScope 버전) */
fun DrawScope.drawFlipGradient(color: Color) {
    val height = 260.dp.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(color, color.copy(0f)),
            startY = 0f,
            endY = height
        ),
        size = Size(this.size.width, height),
    )
}

@Preview(showBackground = true)
@Composable
private fun FlipGradientPreview() {
    Box(modifier = Modifier
        .size(343.dp, 196.dp)
        .flipGradient(Color(0xFFFFEEEE)))
}

@Preview(showBackground = true)
@Composable
private fun FlipGradientPreview2() {
    Box(
        modifier = Modifier
            .size(343.dp, 196.dp)
            .drawWithContent {
                drawFlipGradient(Color.Green)
                drawContent()
            }
    )
}