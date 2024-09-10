package com.team.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.shimmer.shimmerEffect
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

/**
 * HomeScreen 에서 사용 되는 스켈레톤 스크린
 *
 * @param itemCount CardSection(스켈레톤 컨텐츠) 의 개수
 */
@Composable
fun HomeSkeletonScreen(
    modifier: Modifier = Modifier,
    itemCount: Int = 5
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Top)
    ) {
        for (i in 1..itemCount) {
            CardSection(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun CardSection(modifier: Modifier = Modifier) {

    CompositionLocalProvider(LocalContentColor provides FlipTheme.colors.gray3) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(FlipTheme.colors.gray1)
                .shimmerEffect()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardSectionTop()
                CardSectionMiddle(Modifier.padding(bottom = 16.dp))
                CardSectionBottom()
            }
        }
    }
}

@Composable
private fun CardSectionTop(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(LocalContentColor.current)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(11.dp))
                    .width(64.dp)
                    .height(16.dp)
                    .background(LocalContentColor.current)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(11.dp))
                    .width(94.dp)
                    .height(16.dp)
                    .background(LocalContentColor.current)
            )
        }
    }
}

@Composable
private fun CardSectionMiddle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        for (i in 1..3) {

            val mod = if (i == 1) {
                Modifier
                    .width(194.dp)
                    .height(21.dp)
                    .padding(bottom = 1.dp)
            } else Modifier
                .fillMaxWidth()
                .height(16.dp)

            Box(modifier = Modifier
                .clip(RoundedCornerShape(11.dp))
                .then(mod)
                .background(LocalContentColor.current))
        }
    }
}

@Composable
private fun CardSectionBottom(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(11.dp))
                .width(60.dp)
                .height(16.dp)
                .background(LocalContentColor.current)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(11.dp))
                    .width(45.dp)
                    .height(16.dp)
                    .background(LocalContentColor.current)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(11.dp))
                    .width(45.dp)
                    .height(16.dp)
                    .background(LocalContentColor.current)
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(16.dp)
                    .background(LocalContentColor.current)
            )
        }
    }
}

@Preview
@Composable
private fun CardSectionPreview() {
    FlipAppTheme {
        CardSection()
    }
}

@Preview
@Composable
private fun HomeSkeletonScreenPreview() {
    FlipAppTheme {
        Box(modifier = Modifier.background(Color.White)) {
            HomeSkeletonScreen()
        }
    }
}