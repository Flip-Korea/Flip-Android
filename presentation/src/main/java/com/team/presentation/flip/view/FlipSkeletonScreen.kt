package com.team.presentation.flip.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.skeleton.SkeletonBox
import com.team.designsystem.component.skeleton.shimmerEffect

/** [FlipScreen] 에서 사용 되는 스켈레톤 스크린 */
@Composable
fun FlipSkeletonScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.shimmerEffect(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopSection(Modifier.fillMaxWidth())
        MiddleSection()
        BottomSection(Modifier.fillMaxWidth())
    }
}

@Composable
private fun TopSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SkeletonBox(size = DpSize(160.dp, 24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SkeletonBox(size = DpSize(60.dp, 16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonBox(size = DpSize(45.dp, 16.dp))
                SkeletonBox(size = DpSize(45.dp, 16.dp))
            }
        }
    }
}

@Composable
private fun MiddleSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        for (i in 1..4) {
            val size = if (i == 4) DpSize(240.dp, 16.dp) else DpSize(326.dp, 16.dp)
            SkeletonBox(size = size)
        }
    }
}

@Composable
fun BottomSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBox(size = DpSize(40.dp, 40.dp), shape = CircleShape)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SkeletonBox(size = DpSize(64.dp, 16.dp))
                SkeletonBox(size = DpSize(94.dp, 12.dp))
            }
            SkeletonBox(size = DpSize(81.dp, 30.dp), shape = RoundedCornerShape(4.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SkeletonBox(size = DpSize(88.dp, 26.dp), shape = RoundedCornerShape(4.dp))
            SkeletonBox(size = DpSize(70.dp, 26.dp), shape = RoundedCornerShape(4.dp))
            SkeletonBox(size = DpSize(74.dp, 26.dp), shape = RoundedCornerShape(4.dp))
        }
    }
}

@Preview
@Composable
private fun TopSectionPreview() {
    TopSection(Modifier.fillMaxWidth())
}

@Preview
@Composable
private fun MiddleSectionPreview() {
    MiddleSection()
}

@Preview
@Composable
private fun BottomSectionPreview() {
    BottomSection(Modifier.fillMaxWidth())
}

@Preview(showBackground = true)
@Composable
private fun FlipSkeletonScreenPreview() {
    FlipSkeletonScreen(Modifier.fillMaxSize())
}