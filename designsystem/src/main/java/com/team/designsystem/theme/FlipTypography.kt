package com.team.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.team.designsystem.R

data class FlipTypography(
    val headline8: TextStyle = TextStyle(),
    val headline7: TextStyle = TextStyle(),
    val headline6: TextStyle = TextStyle(),
    val headline5: TextStyle = TextStyle(),
    val headline4: TextStyle = TextStyle(),
    val headline3: TextStyle = TextStyle(),
    val headline2: TextStyle = TextStyle(),
    val headline1: TextStyle = TextStyle(),
    val body7: TextStyle = TextStyle(),
    val body6: TextStyle = TextStyle(),
    val body5: TextStyle = TextStyle(),
    val body4Underline: TextStyle = TextStyle(),
    val body3: TextStyle = TextStyle(),
    val body2: TextStyle = TextStyle(),
    val body1: TextStyle = TextStyle(),
)

@Composable
fun headline8(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
//        letterSpacing = (-0.6).sp
    )
}

@Composable
fun headline7(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
//        letterSpacing = (-0.6).sp
    )
}

@Composable
fun headline6(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
//        letterSpacing = (-0.6).sp
    )
}

@Composable
fun headline5(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
//        letterSpacing = (-0.6).sp
    )
}

@Composable
fun headline4(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun headline3(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun headline2(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun headline1(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun body7(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight(400),
        fontSize = 18.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun body6(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun body5(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun body4Underline(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        textDecoration = TextDecoration.Underline,
    )
}

@Composable
fun body3(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun body2(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}

@Composable
fun body1(): TextStyle {
    return TextStyle(
        color = FlipTheme.colors.main,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 1.5.em,
        letterSpacing = (-0.006).em,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
}
