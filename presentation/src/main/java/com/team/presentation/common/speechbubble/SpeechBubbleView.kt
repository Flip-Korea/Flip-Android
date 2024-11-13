package com.team.presentation.common.speechbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

/**
 * SpeechBubble(말풍선) 모양의 Composable
 *
 * @param containerColor 말풍선 배경 색
 * @param text 말풍선 텍스트
 * @param tipStartOffset 팁 위치 (왼쪽 부터 0.dp)
 */
@Composable
fun SpeechBubbleView(
    modifier: Modifier = Modifier,
    containerColor: Color,
    text: String,
    tipStartOffset: Dp = 36.dp,
) {

    val tipHeight by remember { mutableStateOf(12.5.dp) }

    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(SpeechBubbleShape(tipHeight = tipHeight, leftSpacePx = tipStartOffset))
            .background(containerColor)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    top = tipHeight + 11.dp,
                    bottom = 11.dp,
                    start = 17.dp,
                    end = 17.dp
                ),
            text = text,
            style = FlipTheme.typography.body3,
            color = FlipTheme.colors.gray1,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeechBubbleWrapperPreview() {
    FlipAppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SpeechBubbleView(
                modifier = Modifier,
                containerColor = FlipTheme.colors.main.copy(.9f),
                text = "관심분야를 자유롭게 추가하고 삭제해보세요.\n" +
                        "꾹 누르면 관심 있는 순서대로 배치할 수도 있어요!"
            )
            SpeechBubbleView(
                modifier = Modifier,
                containerColor = FlipTheme.colors.main.copy(.9f),
                text = "AAA",
                tipStartOffset = 20.dp
            )
            SpeechBubbleView(
                modifier = Modifier,
                containerColor = FlipTheme.colors.main.copy(.9f),
                text = "관심분야를 자유롭게 추가하고 삭제해보세요.\n" +
                        "꾹 누르면 관심 있는 순서대로 배치할 수도 있어요!" +
                        "관심분야를 자유롭게 추가하고 삭제해보세요.\n" +
                        "꾹 누르면 관심 있는 순서대로 배치할 수도 있어요!" +
                        "관심분야를 자유롭게 추가하고 삭제해보세요.\n" +
                        "꾹 누르면 관심 있는 순서대로 배치할 수도 있어요!"
            )
        }
    }
}