package com.team.presentation.util.composable

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

/**
 * Flip 뒤로가기 (감지) 핸들러
 *
 * @param condition [condition]이 true면, [onBack]을 실행하고 [condition]이 false면, [onBackPressed]를 실행
 * @param onBack 뒤로가기 전 수행할 작업
 * @param onBackPressed 뒤로가기 시 작업
 */
@Composable
fun FlipBackHandler(
    modifier: Modifier = Modifier,
    condition: Boolean,
    onBack: () -> Unit,
    onBackPressed: () -> Unit,
) {

    // 뒤로가기 방식 1
//    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = !backPressHandled) {
        backPressHandled = true
        coroutineScope.launch {
            awaitFrame()
//            onBackPressedDispatcher?.onBackPressed()
            if (condition) {
                onBack()
            } else {
                onBackPressed()
            }
            backPressHandled = false
        }
    }
}