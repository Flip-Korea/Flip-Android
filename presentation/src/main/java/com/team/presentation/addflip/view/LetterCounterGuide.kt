package com.team.presentation.addflip.view

import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipLightColors
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R

/**
 * 글자 수 도우미
 *
 * @param length 글자 길이
 * @param limit 글자 제한 길이
 * @param progress length / limit (비율)
 */
@Composable
fun LetterCounterGuide(
    modifier: Modifier = Modifier,
    length: Float,
    limit: Float,
    progress: Float,
) {

    val color = when {
        ZeroStep >= progress -> { FlipLightColors.gray7 }
        progress in FirstStepStart..FirstStepEnd -> { FlipLightColors.statusBlue }
        progress in SecondStepStart..SecondStepEnd -> { FlipLightColors.statusBlue }
        ThirdStepStart <= progress && progress < ThirdStepEnd -> { FlipLightColors.point }
        else -> {
            /** progress >= 1f */
            FlipLightColors.statusRed
        }
    }

    val guideline = when {
        ZeroStep >= progress -> { R.string.add_flip_screen_letter_guide_notice_1 }
        progress in FirstStepStart..FirstStepEnd -> { R.string.add_flip_screen_letter_guide_notice_2 }
        progress in SecondStepStart..SecondStepEnd -> { R.string.add_flip_screen_letter_guide_notice_3 }
        ThirdStepStart <= progress && progress < ThirdStepEnd -> { R.string.add_flip_screen_letter_guide_notice_4 }
        else -> {
            /** progress >= 1f */
            R.string.add_flip_screen_letter_guide_notice_5
        }
    }
    val guideline2 = when {
        ZeroStep >= progress -> { R.string.add_flip_screen_letter_guide_notice_1_2 }
        progress in FirstStepStart..FirstStepEnd -> { R.string.add_flip_screen_letter_guide_notice_2_2 }
        progress in SecondStepStart..SecondStepEnd -> { R.string.add_flip_screen_letter_guide_notice_3_2 }
        ThirdStepStart <= progress && progress < ThirdStepEnd -> { R.string.add_flip_screen_letter_guide_notice_4_2 }
        else -> {
            /** progress >= 1f */
            R.string.add_flip_screen_letter_guide_notice_5_2
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FlipTheme.colors.gray1)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TitleBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            length, limit, color
        )
        LetterProgressBar(
            modifier = Modifier.fillMaxWidth(),
            color = color,
            progress = progress
        )
        NoticeBar(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            guideline = guideline,
            guideline2 = guideline2,
            color = color
        )
    }
}

@Composable
private fun TitleBar(
    modifier: Modifier = Modifier,
    length: Float,
    limit: Float,
    color: Color
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            text = stringResource(id = R.string.add_flip_screen_letter_guide_title),
            style = FlipTheme.typography.headline2
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End),
            style = FlipTheme.typography.body5,
            color = FlipTheme.colors.gray5,
            text = buildAnnotatedString {
                withStyle(SpanStyle(
                    color = color,
                    fontWeight = FlipTheme.typography.headline1.fontWeight,
                    fontSize = FlipTheme.typography.headline1.fontSize,
                )) {
                    append("${length.toInt()}")
                }
                append("/${limit.toInt()}")
            },
        )
    }
}

@Composable
private fun LetterProgressBar(
    modifier: Modifier = Modifier,
    color: Color,
    progress: Float
) {

    Box(modifier = modifier.height(4.dp)) {
        // 배경 선
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = FlipLightColors.gray1,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 4.dp.toPx()
            )
            drawCircle(
                color = if (progress >= 1f || progress >= ZeroStep) color else FlipLightColors.gray1,
                radius = 4.dp.toPx(),
                center = Offset(0f + 2.dp.toPx(), size.height / 2)
            )
            drawCircle(
                color = if (progress >= 1f || progress >= FirstStepEnd) color else FlipLightColors.gray1,
                radius = 4.dp.toPx(),
                center = Offset(size.width / 3, size.height / 2)
            )
            drawCircle(
                color = if (progress >= 1f || progress >= SecondStepEnd) color else FlipLightColors.gray1,
                radius = 4.dp.toPx(),
                center = Offset(size.width * 2 / 3, size.height / 2)
            )
            drawCircle(
                color = if (progress >= ThirdStepEnd) color else FlipLightColors.gray1,
                radius = 4.dp.toPx(),
                center = Offset(size.width, size.height / 2)
            )
        }

        // 진행 선
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = color,
                start = Offset(0f, size.height / 2),
                end = Offset((size.width * progress).coerceAtMost(size.width), size.height / 2),
                strokeWidth = 4.dp.toPx()
            )
            drawCircle(
                color = color,
                center = Offset(size.width * progress, size.height / 2)
            )
        }
    }
}

@Composable
private fun NoticeBar(
    modifier: Modifier = Modifier,
    @StringRes guideline: Int,
    @StringRes guideline2: Int,
    color: Color
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Start,
        style = FlipTheme.typography.body5,
        color = color,
        text = buildAnnotatedString {
            withStyle(SpanStyle(
                fontSize = FlipTheme.typography.headline2.fontSize,
                fontWeight = FlipTheme.typography.headline2.fontWeight
            )) {
                append(stringResource(id = guideline))
            }
            append(" ${stringResource(id = guideline2)}")
        }
    )
}

private const val ZeroStep = 0f
private const val FirstStepStart = 0.003f
private const val FirstStepEnd = 0.33f
private const val SecondStepStart = 0.33f
private const val SecondStepEnd = 0.66f
private const val ThirdStepStart = 0.66f
private const val ThirdStepEnd = 1f

@Preview(showBackground = true)
@Composable
fun LetterCounterGuidePreview() {

    FlipAppTheme {
        Box(modifier = Modifier) {
            LetterCounterGuide(
                length = 130f,
                limit = 300f,
                progress = 0.5f
            )
        }
    }
}