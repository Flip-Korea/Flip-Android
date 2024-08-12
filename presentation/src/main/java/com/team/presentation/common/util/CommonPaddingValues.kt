package com.team.presentation.common.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

/**
 * 모든 화면에서 공통적으로 사용되는 패딩
 *
 * Each Platform StatusBar Height
 *
 * iOS: 약 42(dp)
 * Android: 약 20(dp)
 *
 * TopBarVertical: ((iOS - Android) / 2) 근사값
 */
internal object CommonPaddingValues {
    /** 10 dp */
    val TopBarVertical = 10.dp
    /** 6 dp */
    private val TopBarHorizontal = 6.dp
    /** 16 dp */
    val HorizontalPadding = 16.dp
    /**
     * horizontal: TopBarHorizontalPadding
     * vertical: TopBarVerticalPadding
     * @see TopBarHorizontal
     * @see TopBarVertical
     */
    val TopBarWithTouchTarget = PaddingValues(
        horizontal = TopBarHorizontal,
        vertical = TopBarVertical
    )
    /**
     * start: [CommonPaddingValues.TopBarHorizontal]
     *
     * end: 7 dp
     *
     * top: [CommonPaddingValues.TopBarVertical]
     *
     * bottom: [CommonPaddingValues.TopBarVertical]
     */
    val TopBarWithLogo = PaddingValues(
        start = 16.dp,
        end = 7.dp,
        top = TopBarVertical,
        bottom = TopBarVertical
    )
}