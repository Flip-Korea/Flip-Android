package com.team.designsystem.component.textfield

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.team.designsystem.theme.FlipLightColors

object FlipTextFieldStyles {
    val cursorBrushPoint = Brush.verticalGradient(
        0.0f to Color.Transparent,
        0.10f to Color.Transparent,
        0.10f to FlipLightColors.point,
        0.90f to FlipLightColors.point,
        0.90f to Color.Transparent,
        1.0f to Color.Transparent,
    )

    val cursorBrushBlack = Brush.verticalGradient(
        0.0f to Color.Transparent,
        0.10f to Color.Transparent,
        0.10f to FlipLightColors.main,
        0.90f to FlipLightColors.main,
        0.90f to Color.Transparent,
        1.0f to Color.Transparent,
    )
}