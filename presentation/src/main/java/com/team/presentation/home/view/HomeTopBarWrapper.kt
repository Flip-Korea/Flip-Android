package com.team.presentation.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun HomeTopBarWrapper(
    modifier: Modifier = Modifier,
    topBarOffsetHeightPx: Float,
    content: @Composable () -> Unit,
) {

    Column(
        modifier = modifier
            .graphicsLayer(
                translationX = 0f,
                translationY = topBarOffsetHeightPx
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        content()
    }
}