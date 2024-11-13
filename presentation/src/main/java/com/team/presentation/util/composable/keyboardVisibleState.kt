package com.team.presentation.util.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity

/**
 * 키보드 활성화 감지 상태
 */
@Composable
fun keyboardVisibleState(): State<Boolean> {
//    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val isImeVisible = WindowInsets.isImeVisible
    return rememberUpdatedState(isImeVisible)
}

private val WindowInsets.Companion.isImeVisible: Boolean
    @Composable
    get() {
        val density = LocalDensity.current
        val ime = this.ime
        return remember {
            derivedStateOf {
                ime.getBottom(density) > 0
            }
        }.value
    }
