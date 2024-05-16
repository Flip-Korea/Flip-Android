package com.team.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val localColorScheme = staticCompositionLocalOf { FlipLightColors }
private val localShapes = staticCompositionLocalOf { FlipShapes() }
private val localTypography = staticCompositionLocalOf { FlipTypography() }
private val localTransition = staticCompositionLocalOf { FlipTransition() }
//private val localRipple = staticCompositionLocalOf { FlipRipple() }

object FlipTheme {
    val colors: FlipColors
        @Composable
        @ReadOnlyComposable
        get() = localColorScheme.current

    val typography: FlipTypography
        @Composable
        @ReadOnlyComposable
        get() = localTypography.current

    val shapes: FlipShapes
        @Composable
        @ReadOnlyComposable
        get() = localShapes.current

    val transition: FlipTransition
        @Composable
        @ReadOnlyComposable
        get() = localTransition.current
}


//TODO FlipColors 등의 클래스들에게 @Immutable 부여하기
@Composable
fun FlipAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colorScheme = if (darkTheme) FlipLightColors else FlipLightColors

    val typography = FlipTypography(
        headline8 = headline8(),
        headline7 = headline7(),
        headline6 = headline6(),
        headline5 = headline5(),
        headline4 = headline4(),
        headline3 = headline3(),
        headline2 = headline2(),
        headline1 = headline1(),
        body7 = body7(),
        body6 = body6(),
        body5 = body5(),
        body4Underline = body4Underline(),
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
        localShapes provides FlipShapes(),
        localTransition provides FlipTransition(),
        LocalRippleTheme provides FlipRipple(),
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