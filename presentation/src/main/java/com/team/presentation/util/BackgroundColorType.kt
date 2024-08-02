package com.team.presentation.util

import androidx.compose.ui.graphics.Color

enum class BackgroundColorType {
    DEFAULT,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE
}

fun BackgroundColorType.asString(): String =
    when (this) {
        BackgroundColorType.DEFAULT -> "기본"
        BackgroundColorType.RED -> "레드"
        BackgroundColorType.YELLOW -> "옐로우"
        BackgroundColorType.GREEN -> "그린"
        BackgroundColorType.BLUE -> "블루"
        BackgroundColorType.PURPLE -> "퍼플"
    }

fun BackgroundColorType.asColor(): Color =
    when (this) {
        BackgroundColorType.DEFAULT -> Color(0xFFFFFFFF)
        BackgroundColorType.RED -> Color(0xFFFFE1E1)
        BackgroundColorType.YELLOW -> Color(0xFFFFF5E1)
        BackgroundColorType.GREEN -> Color(0xFFEDFFE1)
        BackgroundColorType.BLUE -> Color(0xFFE1FDFF)
        BackgroundColorType.PURPLE -> Color(0xFFF2E1FF)
    }