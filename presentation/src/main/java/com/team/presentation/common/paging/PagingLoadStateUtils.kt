package com.team.presentation.common.paging

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.team.data.paging.FlipPagingException
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText

/** 페이징 아이템 로드가 끝났을 시 (성공 시) */
fun LazyPagingItems<*>.isSuccess(): Boolean =
    this.loadState.refresh is LoadState.NotLoading ||
            !this.loadState.hasError ||
            this.loadState.append is LoadState.NotLoading

/** 페이징 아이템 로딩 시 */
fun LazyPagingItems<*>.isLoading(): Boolean =
    this.loadState.append is LoadState.Loading ||
            this.loadState.prepend is LoadState.Loading ||
            this.loadState.refresh is LoadState.Loading

/** 페이징 아이템의 끝에 도달했을 시 */
fun LazyPagingItems<*>.isEndOfPaginationReached(): Boolean {
    val appendNotLoading = this.loadState.append is LoadState.NotLoading
    val appendEndOfPaginationReached = this.loadState.append.endOfPaginationReached
    return appendNotLoading && appendEndOfPaginationReached
}

/** 페이징 아이템 오류 발생 시 */
fun LazyPagingItems<*>.isError(): UiText? {
    val errorState = this.loadState.append as? LoadState.Error
        ?: this.loadState.prepend as? LoadState.Error
        ?: this.loadState.refresh as? LoadState.Error
    return when (errorState) {
        is LoadState.Error -> {
            val errorText = (errorState.error as? FlipPagingException)
                ?.errorType
                ?.asUiText()
            errorText
        }

        else -> null
    }
}