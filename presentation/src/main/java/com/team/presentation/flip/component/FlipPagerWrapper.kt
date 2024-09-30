package com.team.presentation.flip.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Flip(Short Form)들을 수직으로 스와이프 할 수 있게 해준다. (VerticalPager 사용)
 *
 * @param content 플립(숏폼) 컨텐츠
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlipPagerWrapper(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    content: @Composable () -> Unit
) {
    VerticalPager(
        modifier = modifier,
        state = pagerState
    ) {
        content()
    }
}