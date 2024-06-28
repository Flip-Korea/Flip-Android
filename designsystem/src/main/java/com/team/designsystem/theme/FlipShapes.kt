package com.team.designsystem.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

data class FlipShapes(
    val roundedCornerSmall: CornerBasedShape = RoundedCornerShape(4.dp),
    val roundedCornerMedium: CornerBasedShape = RoundedCornerShape(8.dp),
    val roundedCornerLarge: CornerBasedShape = RoundedCornerShape(12.dp),
    val roundedCornerExtraLarge: CornerBasedShape = RoundedCornerShape(24.dp),
    val roundedCornerTextField: CornerBasedShape = RoundedCornerShape(6.dp),
    val roundedCornerFlipCard: CornerBasedShape = RoundedCornerShape(6.dp)
)
