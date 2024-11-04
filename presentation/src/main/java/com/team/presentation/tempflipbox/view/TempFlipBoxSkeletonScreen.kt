package com.team.presentation.tempflipbox.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.skeleton.shimmerEffect
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

/**
 * TempFlipBoxScreen 에서 사용되는 스켈레톤 스크린
 *
 * @param itemCount CardSection(스켈레톤 컨텐츠) 의 개수
 */
@Composable
fun TempFlipBoxSkeletonScreen(
    modifier: Modifier = Modifier,
    itemCount: Int,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(13.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .clip(RoundedCornerShape(11.dp))
                .size(36.dp, 18.dp)
                .shimmerEffect()
                .background(FlipTheme.colors.gray3)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardSectionList(itemCount)
        }
    }
}

@Composable
private fun CardSectionList(itemCount: Int) {
    for (i in 1..itemCount) {
        CardSection(
            modifier = Modifier.fillMaxWidth(),
            containerColor = FlipTheme.colors.gray1,
            contentColor = FlipTheme.colors.gray3
        )
    }
}

@Composable
private fun CardSection(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(containerColor)
            .shimmerEffect()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .clip(RoundedCornerShape(11.dp))
                    .size(194.dp, 18.dp)
                    .background(contentColor)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .clip(RoundedCornerShape(11.dp))
                    .size(60.dp, 18.dp)
                    .background(contentColor)
            )
        }
    }
}

@Preview
@Composable
private fun CardSectionPreview() {
    FlipAppTheme {
        CardSection(containerColor = FlipTheme.colors.gray3, contentColor = FlipTheme.colors.gray1)
    }
}

@Preview(showBackground = true, backgroundColor = 0x00FFFFFF)
@Composable
private fun TempFlipBoxSkeletonScreenPreview() {
    FlipAppTheme {
        TempFlipBoxSkeletonScreen(itemCount = 3)
    }
}