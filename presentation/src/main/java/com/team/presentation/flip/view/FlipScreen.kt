package com.team.presentation.flip.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FlipScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize())
}

@Preview
@Composable
private fun FlipScreenPreview() {
    FlipScreen()
}