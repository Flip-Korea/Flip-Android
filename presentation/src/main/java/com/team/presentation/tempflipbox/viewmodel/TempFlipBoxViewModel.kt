package com.team.presentation.tempflipbox.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.team.domain.usecase.temppost.TempPostUseCases
import com.team.domain.util.Result
import com.team.domain.util.SuccessType
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TempFlipBoxViewModel @Inject constructor(
    private val tempPostUseCases: TempPostUseCases,
): FlipBaseViewModel<TempFlipBoxContract.UiState, TempFlipBoxContract.UiEvent, TempFlipBoxContract.UiEffect>() {

    private var cursor: MutableState<Long?> = mutableStateOf(null)

    init {
        getTempPosts()
    }

    override fun createInitialState(): TempFlipBoxContract.UiState = TempFlipBoxContract.UiState.Idle

    override suspend fun handleEvent(event: TempFlipBoxContract.UiEvent) {
        when (event) {
            TempFlipBoxContract.UiEvent.NavigateToBack -> TODO()
            TempFlipBoxContract.UiEvent.NavigateToPostDetail -> TODO()
            is TempFlipBoxContract.UiEvent.OnTempPostsDelete -> { deleteTempPosts(event.tempPostIds) }
            TempFlipBoxContract.UiEvent.GetTempPosts -> { getTempPosts() }
        }
    }

    /**
     * 임시저장플립 리스트 조회
     */
    private fun getTempPosts() {
        updateState { TempFlipBoxContract.UiState.Loading }
        viewModelScope.launch {
            tempPostUseCases.getTempPostsUseCase(cursor.value.toString(), TEMP_POST_PAGE_SIZE).onEach { result ->
                when (result) {
                    Result.Loading -> {
                        updateState { TempFlipBoxContract.UiState.Loading }
                    }
                    is Result.Error -> {
                        updateState { TempFlipBoxContract.UiState.Error(errorBodyFirst(result.errorBody, result.error)) }
                    }
                    is Result.Success -> {
                        if (result.data.tempPosts.isNotEmpty()) {
                            // 마지막 Post의 ID를 커서로 저장
                            cursor.value = result.data.tempPosts.last().tempPostId
                        }
                        updateState { TempFlipBoxContract.UiState.TempPosts(result.data.tempPosts) }
                    }
                }
            }.launchIn(this)
        }
    }

    /**
     * 임시저장플립 삭제
     */
    private fun deleteTempPosts(tempPostIds: List<Long>) {
        //TODO: 로딩처리는 어떻게?
        var results = emptyList<Pair<Boolean, UiText>>()

        viewModelScope.launch {
            tempPostIds.map { tempPostId ->
                async {
                    tempPostUseCases.deleteTempPostUseCase(tempPostId).onEach { result ->
                        when (result) {
                            Result.Loading -> { }
                            is Result.Error -> {
                                results = results.toMutableList().apply {
                                    add(Pair(false, errorBodyFirst(result.errorBody, result.error)))
                                }
                            }
                            is Result.Success -> {
                                results = results.toMutableList().apply { add(Pair(true, UiText.DynamicString(""))) }

                            }
                        }
                    }.launchIn(this)
                }
            }.awaitAll()

            val filteredResult: List<Pair<Boolean, UiText>> = results.filter { !it.first }
            showSnackbar(
                message = if (filteredResult.isNotEmpty()) {
                    filteredResult.last().second
                } else { SuccessType.TempPost.DELETE.asUiText() }
            )
        }
    }
}

private suspend fun showSnackbar(
    message: UiText
) {
    SnackbarController.sendEvent(
        event = SnackbarEvent(
            message = message
        )
    )
}

private const val TEMP_POST_PAGE_SIZE = 5