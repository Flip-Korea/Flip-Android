package com.team.presentation.util

import androidx.compose.ui.graphics.Color
import com.team.domain.type.BackgroundColorType

fun BackgroundColorType.asColor(): Color =
    when (this) {
        BackgroundColorType.DEFAULT -> Color(0xFFFFFFFF)
        BackgroundColorType.RED -> Color(0xFFFFE1E1)
        BackgroundColorType.YELLOW -> Color(0xFFFFF5E1)
        BackgroundColorType.GREEN -> Color(0xFFEDFFE1)
        BackgroundColorType.BLUE -> Color(0xFFE1FDFF)
        BackgroundColorType.PURPLE -> Color(0xFFF2E1FF)
    }