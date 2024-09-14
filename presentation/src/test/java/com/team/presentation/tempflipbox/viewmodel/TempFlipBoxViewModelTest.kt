package com.team.presentation.tempflipbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.team.domain.usecase.temppost.AddTempPostUseCase
import com.team.domain.usecase.temppost.DeleteTempPostUseCase
import com.team.domain.usecase.temppost.EditTempPostUseCase
import com.team.domain.usecase.temppost.GetTempPostsPaginationUseCase
import com.team.domain.usecase.temppost.TempPostUseCases
import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.TestDispatcherRule
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.tempflipbox.TempFlipBoxContract
import com.team.presentation.tempflipbox.testdoubles.tempPostListTestData
import com.team.presentation.util.uitext.UiText
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class TempFlipBoxViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()
    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tempFlipBoxViewModel: TempFlipBoxViewModel
    private val addTempPostUseCase: AddTempPostUseCase = mockk()
    private val getTempPostsUseCase: GetTempPostsPaginationUseCase = mockk()
    private val editTempPostUseCase: EditTempPostUseCase = mockk()
    private val deleteTempPostUseCase: DeleteTempPostUseCase = mockk()
    private val tempPostUseCases: TempPostUseCases = TempPostUseCases(addTempPostUseCase, getTempPostsUseCase, editTempPostUseCase, deleteTempPostUseCase)

    @Test
    fun `임시저장함 목록 조회 실패 (getTempPosts())`() = runTest {
        // Given
        val actualErrorBody = ErrorBody("", emptyList(), "error")
        var cursor: MutableState<Long?> = mutableStateOf(null)
        every {
            getTempPostsUseCase(cursor.value.toString(), TEMP_POST_PAGE_SIZE)
        } returns flowOf(Result.Error(
            error = ErrorType.Network.INTERNAL_SERVER_ERROR,
            errorBody = actualErrorBody)
        )

        var uiState: TempFlipBoxContract.UiState? = null
        val job = launch {
            tempFlipBoxViewModel.uiState.collectLatest {
                uiState = it
            }
        }

        // When
        tempFlipBoxViewModel = TempFlipBoxViewModel(tempPostUseCases)
        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        when (uiState) {
            is TempFlipBoxContract.UiState.Error -> {
                val expectedError = (uiState as TempFlipBoxContract.UiState.Error).error
                assertEquals(
                    UiText.DynamicString(actualErrorBody.message),
                    expectedError
                )
            }
            else -> { assert(true) }
        }
    }

    @Test
    fun `임시저장함 목록 조회 성공 (getTempPosts())`() = runTest {
        // Given
        var cursor: MutableState<Long?> = mutableStateOf(null)
        every {
            getTempPostsUseCase(cursor.value.toString(), TEMP_POST_PAGE_SIZE)
        } returns flowOf(Result.Success(tempPostListTestData))

        var uiState: TempFlipBoxContract.UiState? = null
        val job = launch {
            tempFlipBoxViewModel.uiState.collectLatest {
                uiState = it
            }
        }

        // When
        tempFlipBoxViewModel = TempFlipBoxViewModel(tempPostUseCases)
        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        when (uiState) {
            is TempFlipBoxContract.UiState.TempPosts -> {
                val actualResult = tempPostListTestData.tempPosts
                val expectedResult = (uiState as TempFlipBoxContract.UiState.TempPosts).tempPosts
                assertEquals(expectedResult, actualResult)
            }
            else -> { assert(true) }
        }
    }

    @Test
    fun `임시저장플립 삭제 실패 (deleteTempPosts())`() = runTest {
        // Given
        val actualErrorBody = ErrorBody("", emptyList(), "error")
        var cursor: MutableState<Long?> = mutableStateOf(null)
        val tempPostId: Long = 1
        every {
            getTempPostsUseCase(cursor.value.toString(), TEMP_POST_PAGE_SIZE)
        } returns flowOf(Result.Success(tempPostListTestData))
        every {
            deleteTempPostUseCase(tempPostId)
        } returns flowOf(Result.Error(
            error = ErrorType.Network.INTERNAL_SERVER_ERROR,
            errorBody = actualErrorBody
        ))

        var snackbarEvent: SnackbarEvent? = null
        val job = launch {
            SnackbarController.events.collectLatest {
                snackbarEvent = it
            }
        }

        // When
        tempFlipBoxViewModel = TempFlipBoxViewModel(tempPostUseCases)
        tempFlipBoxViewModel.processEvent(
            TempFlipBoxContract.UiEvent.OnTempPostsDelete(listOf(tempPostId))
        )
        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        assertEquals(
            snackbarEvent?.message,
            UiText.DynamicString(actualErrorBody.message)
        )
    }

    @Test
    fun `임시저장플립 삭제 성공 (deleteTempPosts())`() = runTest {
        // Given
        var cursor: MutableState<Long?> = mutableStateOf(null)
        val tempPostId: Long = 1
        every {
            getTempPostsUseCase(cursor.value.toString(), TEMP_POST_PAGE_SIZE)
        } returns flowOf(Result.Success(tempPostListTestData))
        every {
            deleteTempPostUseCase(tempPostId)
        } returns flowOf(Result.Success(true))

        var snackbarEvent: SnackbarEvent? = null
        val job = launch {
            SnackbarController.events.collectLatest {
                snackbarEvent = it
            }
        }

        // When
        tempFlipBoxViewModel = TempFlipBoxViewModel(tempPostUseCases)
        tempFlipBoxViewModel.processEvent(
            TempFlipBoxContract.UiEvent.OnTempPostsDelete(listOf(tempPostId))
        )
        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        assert(snackbarEvent != null)
    }
}

private const val TEMP_POST_PAGE_SIZE = 5