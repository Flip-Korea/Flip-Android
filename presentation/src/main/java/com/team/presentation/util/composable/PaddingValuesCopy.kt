package com.team.presentation.util.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.copy(
    start: Dp? = null,
    end: Dp? = null,
    top: Dp? = null,
    bottom: Dp? = null,
): PaddingValues {
    val localLayoutDirection = LocalLayoutDirection.current
    val leftPadding = start ?: this.calculateLeftPadding(localLayoutDirection)
    val rightPadding = end ?: this.calculateRightPadding(localLayoutDirection)
    val topPadding = top ?: this.calculateTopPadding()
    val bottomPadding = bottom ?: this.calculateBottomPadding()
    val newPaddingValues =
        PaddingValues(
            start = leftPadding,
            end = rightPadding,
            top = topPadding,
            bottom = bottomPadding,
        )
    return newPaddingValues
}
