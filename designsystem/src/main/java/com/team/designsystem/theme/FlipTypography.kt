package com.team.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.team.designsystem.R

data class FlipTypography(
    val headline5: TextStyle = TextStyle(),
    val headline4: TextStyle = TextStyle(),
    val headline3: TextStyle = TextStyle(),
    val headline2: TextStyle = TextStyle(),
    val headline1: TextStyle = TextStyle(),
    val body5: TextStyle = TextStyle(),
    val body4: TextStyle = TextStyle(),
    val body3: TextStyle = TextStyle(),
    val body2: TextStyle = TextStyle(),
    val body1: TextStyle = TextStyle(),
)

@Composable
fun headline5(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp * 1.5,
        letterSpacing = 24.sp * (-0.006),
//        letterSpacing = (-0.6).sp
    )
}

@Composable
fun headline4(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp * 1.5,
        letterSpacing = 24.sp * (-0.006),
    )
}

@Composable
fun headline3(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp * 1.5,
        letterSpacing = 16.sp * (-0.006),
    )
}

@Composable
fun headline2(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp * 1.5,
        letterSpacing = 16.sp * (-0.006),
    )
}

@Composable
fun headline1(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp * 1.5,
        letterSpacing = 14.sp * (-0.006),
    )
}

@Composable
fun body5(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 18.sp * 1.5,
        letterSpacing = 18.sp * (-0.006),
    )
}

@Composable
fun body4(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 16.sp * 1.5,
        letterSpacing = 16.sp * (-0.006),
    )
}

@Composable
fun body3(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp * 1.5,
        letterSpacing = 14.sp * (-0.006),
    )
}

@Composable
fun body2(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp * 1.5,
        letterSpacing = 12.sp * (-0.006),
    )
}

@Composable
fun body1(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 10.sp * 1.5,
        letterSpacing = 10.sp * (-0.006),
    )
}
