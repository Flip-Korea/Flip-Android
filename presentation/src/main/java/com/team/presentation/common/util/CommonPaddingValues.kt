package com.team.presentation.common.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

internal object CommonPaddingValues {
    val TopBarVertical = 14.dp
    val TopBarWithLogo = PaddingValues(start = 16.dp, end = 7.dp, top = TopBarVertical, bottom = TopBarVertical)
    val TopBarWithTouchTarget = PaddingValues(start = 6.dp, end = 6.dp, top = TopBarVertical, bottom = TopBarVertical)
}