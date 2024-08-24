package com.team.presentation.common.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 일회성 이벤트 감지 및 처리를 도와준다.
 *
 * @param flow [Flow] 타입 (LaunchedEffect 의 키 1)
 * @param key1 LaunchedEffect 의 키 2
 * @param key2 LaunchedEffect 의 키 3
 * @param onEvent 이벤트 처리
 *
 * ### 사용 예시
 *     ObserveAsEvents(
 *         flow = SnackbarController.events,
 *         key1 = snackbarHostState
 *     ) { event ->
 *         scope.launch {
 *             snackbarHostState.currentSnackbarData?.dismiss()
 *
 *             val result = snackbarHostState.showSnackbar(
 *                 message = event.message,
 *                 actionLabel = event.action?.name,
 *                 duration = SnackbarDuration.Short
 *             )
 *
 *             if (result == SnackbarResult.ActionPerformed) {
 *                 event.action?.action?.invoke()
 *             }
 *         }
 *     }
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}