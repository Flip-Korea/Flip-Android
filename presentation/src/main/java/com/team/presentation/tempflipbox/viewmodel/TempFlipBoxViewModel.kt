package com.team.presentation.tempflipbox.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.team.domain.usecase.temppost.TempPostUseCases
import com.team.domain.util.Result
import com.team.domain.util.SuccessType
import com.team.presentation.common.paging.FlipLoadState
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.tempflipbox.TempFlipBoxContract
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import com.team.presentation.util.uitext.errorBodyFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TempFlipBoxViewModel @Inject constructor(
    private val tempPostUseCases: TempPostUseCases,
) : FlipBaseViewModel<TempFlipBoxContract.UiState, TempFlipBoxContract.UiEvent, TempFlipBoxContract.UiEffect>() {

    val tempPostPaging = tempPostUseCases.getTempPostsPaginationUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3_000),
            initialValue = PagingData.empty()
        )
        .cachedIn(viewModelScope)

    override fun createInitialState(): TempFlipBoxContract.UiState =
        TempFlipBoxContract.UiState.Idle

    override suspend fun handleEvent(event: TempFlipBoxContract.UiEvent) {
        when (event) {
            TempFlipBoxContract.UiEvent.NavigateToBack -> TODO()
            TempFlipBoxContract.UiEvent.NavigateToPostDetail -> TODO()
            is TempFlipBoxContract.UiEvent.OnTempPostsDelete -> {
                event.tempPostIds?.let { tempPostIds ->
                    deleteTempPosts(tempPostIds)
                } ?: showDialogModal()
            }
        }
    }

    /** UiState와 Paging의 LoadState를 동기화하기 위해 사용 */
    fun updateUiStateWithLoadState(flipLoadState: FlipLoadState) {
        when (flipLoadState) {
            FlipLoadState.Error -> {}
            FlipLoadState.Loading -> {}
            FlipLoadState.NotLoading -> updateState { TempFlipBoxContract.UiState.TempPostSuccess }
        }
    }

    private fun showDialogModal() {
        sendEffect { TempFlipBoxContract.UiEffect.ShowDialogModal }
    }

    /** 임시저장플립 삭제 */
    //TODO: 로딩처리는 어떻게?
    private fun deleteTempPosts(tempPostIds: List<Long>) {
        var results = emptyList<Pair<Boolean, UiText>>()

        viewModelScope.launch {
            tempPostIds.map { tempPostId ->
                async {
                    tempPostUseCases.deleteTempPostUseCase(tempPostId).onEach { result ->
                        when (result) {
                            Result.Loading -> {}
                            is Result.Error -> {
                                results = results.toMutableList().apply {
                                    add(Pair(false, errorBodyFirst(result.errorBody, result.error)))
                                }
                            }

                            is Result.Success -> {
                                results = results.toMutableList()
                                    .apply { add(Pair(true, UiText.DynamicString(""))) }
                            }
                        }
                    }.launchIn(this)
                }
            }.awaitAll()

            val filteredResult: List<Pair<Boolean, UiText>> = results.filter { !it.first }

            showSnackbar(
                message = if (filteredResult.isNotEmpty()) {
                    filteredResult.last().second
                } else {
                    SuccessType.TempPost.DELETE.asUiText()
                }
            )
        }
    }
}

private suspend fun showSnackbar(message: UiText) {
    SnackbarController.sendEvent(event = SnackbarEvent(message = message))
}