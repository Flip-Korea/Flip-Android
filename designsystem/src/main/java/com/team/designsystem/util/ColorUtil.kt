package com.team.designsystem.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/** Compose Color 타입을 Int 타입으로 변환 */
fun Color.toArgbInt(): Int = this.toArgb()

/** Int 타입을 Compose Color 타입으로 변환 */
fun Int.toColor(): Color = Color(this)
