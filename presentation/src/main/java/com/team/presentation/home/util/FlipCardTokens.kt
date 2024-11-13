package com.team.presentation.home.util

import androidx.compose.ui.graphics.Color

object FlipCardTokens {
    val bgColorMap = { id: Int ->
        when(id) {
            0 -> Color(0xFFFFFFFF)
            1 -> Color(0xFFFFE1E1)
            2 -> Color(0xFFFFF5E1)
            3 -> Color(0xFFEDFFE1)
            4 -> Color(0xFFE1FDFF)
            5 -> Color(0xFFF2E1FF)
            else -> Color(0xFFFFFFFF)
        }
    }
}