package com.team.designsystem.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class FlipRipple : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Red

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(1f, 1f, 1f, 1f)
}

class FlipNoRipple : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0f, 0f, 0f, 0f)
}