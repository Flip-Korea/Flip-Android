package com.team.presentation.common.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * Paging의 LoadState 를 통합적으로 관리하기 위해 사용
 * @param lazyPagingItems 페이징 아이템
 * @param updateState [FlipLoadState] 통합 상태를 전달
 */
@Composable
fun <T : Any> HandleLoadState(
    lazyPagingItems: LazyPagingItems<T>,
    updateState: (FlipLoadState) -> Unit,
) {
    val loadStates = remember(lazyPagingItems.loadState) {
        listOf(
            lazyPagingItems.loadState.refresh,
            lazyPagingItems.loadState.prepend,
            lazyPagingItems.loadState.append,
        )
    }

    LaunchedEffect(loadStates) {
        when {
            loadStates.any { it is LoadState.Error } -> {
                updateState(FlipLoadState.Error)
            }

            loadStates.any { it is LoadState.Loading } -> {
                updateState(FlipLoadState.Loading)
            }

            loadStates.any { it is LoadState.NotLoading } -> {
                updateState(FlipLoadState.NotLoading)
            }

            else -> return@LaunchedEffect
        }
    }
}