package com.team.presentation.common.paging

/** Paging의 LoadState를 일관되게 관리 */
sealed interface FlipLoadState {
    data object Error : FlipLoadState
    data object Loading : FlipLoadState
    data object NotLoading : FlipLoadState
}
