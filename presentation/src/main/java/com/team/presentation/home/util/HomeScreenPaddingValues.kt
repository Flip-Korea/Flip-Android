package com.team.presentation.home.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

internal object HomeScreenPaddingValues {
    val Horizontal = PaddingValues(horizontal = 16.dp)
    val TopBarHorizontal = PaddingValues(start = 16.dp, end = 7.dp)
    val TopBarPadding = PaddingValues(start = 16.dp, end = 7.dp, top = 14.dp, bottom = 14.dp)
    /** TopBarPadding 의 top & bottom padding과 똑같아야 함
     * @see TopBarPadding
     */
    val TopBarTopPadding = 14.dp
}