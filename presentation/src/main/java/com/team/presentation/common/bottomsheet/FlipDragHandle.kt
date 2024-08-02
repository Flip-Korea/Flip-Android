package com.team.presentation.common.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipDragHandle(
    modifier: Modifier = Modifier,
    verticalPadding: Dp
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(verticalPadding)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(200.dp))
                .width(48.dp)
                .height(5.dp)
                .background(FlipTheme.colors.gray3)
        )
    }
}