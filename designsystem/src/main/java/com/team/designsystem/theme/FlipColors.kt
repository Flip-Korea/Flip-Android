package com.team.designsystem.theme

import androidx.compose.ui.graphics.Color

// Background Color
//    val bgRed: Color,
//    val bgYellow: Color,
//    val bgGreen: Color,
//    val bgBlue: Color,
//    val bgPurple: Color,

// bgRed = Color(0xFFFFE1E1),
// bgYellow = Color(0xFFFFF5E1),
// bgGreen = Color(0xFFEDFFE1),
// bgBlue = Color(0xFFE1FDFF),
// bgPurple = Color(0xFFF2E1FF),

data class FlipColors(
    // Primary Color
    val main: Color,
    // Gray Scale
    val gray7: Color,
    val gray6: Color,
    val gray5: Color,
    val gray4: Color,
    val gray3: Color,
    val gray2: Color,
    val gray1: Color,
    // Point(Secondary) Color
    val point: Color,
    val point2: Color,
    val point3: Color,
    // Status Color
    val statusRed: Color,
    val statusBlue: Color,
    // Default Color
    val white: Color,
)

val FlipLightColors = FlipColors(
    main = Color(0xFF212121),
    gray7 = Color(0xFF616161),
    gray6 = Color(0xFF757575),
    gray5 = Color(0xFF9E9E9E),
    gray4 = Color(0xFFBDBDBD),
    gray3 = Color(0xFFE0E0E0),
    gray2 = Color(0xFFEEEEEE),
    gray1 = Color(0xFFF6F6F6),
    point = Color(0xFF00AB85),
    point2 = Color(0xFF15E1B3),
    point3 = Color(0xFF9FFAE6),
    statusRed = Color(0xFFFF1F4B),
    statusBlue = Color(0xFF4990FF),
    white = Color(0xFFFFFFFF)
)