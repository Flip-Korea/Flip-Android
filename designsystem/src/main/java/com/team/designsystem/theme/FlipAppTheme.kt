package com.team.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat

private val localColorScheme = staticCompositionLocalOf { FlipLightColors }
private val localRadius = staticCompositionLocalOf { FlipRadius() }
private val localTypography = staticCompositionLocalOf { FlipTypography() }
private val localRipple = staticCompositionLocalOf { FlipRipple() }

object FlipTheme {
    val colors: FlipColors
        @Composable
        @ReadOnlyComposable
        get() = localColorScheme.current

    val typography: FlipTypography
        @Composable
        @ReadOnlyComposable
        get() = localTypography.current

    val radius: FlipRadius
        @Composable
        @ReadOnlyComposable
        get() = localRadius.current
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FlipAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colorScheme = if (darkTheme) FlipLightColors else FlipLightColors

    val typography = FlipTypography(
        headline5 = headline5(),
        headline4 = headline4(),
        headline3 = headline3(),
        headline2 = headline2(),
        headline1 = headline1(),
        body5 = body5(),
        body4 = body4(),
        body3 = body3(),
        body2 = body2(),
        body1 = body1(),
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        localColorScheme provides colorScheme,
        localTypography provides typography,
        localRadius provides FlipRadius(),
//        LocalRippleTheme provides FlipRipple(),
    ) {
        // 1. Default
        content()

        // 2. For UI Testing
//        Box(modifier = Modifier.semantics { testTagsAsResourceId = true }) {
//            content()
//        }
        // Usage
//        Text(
//            modifier = Modifier.fillMaxWidth()
    //            .testTag("testText"),
//            text = ""
//        )
    }
}