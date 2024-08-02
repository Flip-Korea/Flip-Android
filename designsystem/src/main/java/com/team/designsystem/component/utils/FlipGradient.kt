package com.team.designsystem.component.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Flip 카드 및 상세화면에서 사용 되는 그라데이션
 */
fun Modifier.flipGradient(
    color: Color,
    shape: Shape = RoundedCornerShape(3.dp)
) = then(
    background(
        brush = Brush.verticalGradient(listOf(color, color.copy(0f))),
        shape = shape
    )
)

@Preview(showBackground = true)
@Composable
private fun FlipGradientPreview() {

    Box(modifier = Modifier.size(343.dp, 196.dp).flipGradient(Color(0xFFFFEEEE)))
}