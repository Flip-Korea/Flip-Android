package com.team.presentation.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex

/**
 * HomeScreen 에서 TopBar를 감싸고 있는 부분
 *
 * BoxScope 내에서 사용되어야 한다.
 *
 * @param animatedTopBarOffset TopBar(content 부분)의 높이 오프셋 px 값
 * @param content Composable 함수로 TopBar 에 해당 되는 부분
 */
@Composable
fun HomeTopBarWrapper(
    modifier: Modifier = Modifier,
    animatedTopBarOffset: Dp,
    content: @Composable () -> Unit,
) {

    Column(
        modifier = modifier
            .zIndex(1f)
            .offset(y = animatedTopBarOffset),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        content()
    }
}