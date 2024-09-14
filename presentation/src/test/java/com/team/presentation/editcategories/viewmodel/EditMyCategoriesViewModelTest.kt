package com.team.presentation.editcategories.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.domain.DataStoreManager
import com.team.domain.model.category.Category
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.speechbubble.GetSpeechBubbleCountUseCase
import com.team.domain.usecase.speechbubble.IncrementSpeechBubbleCountUseCase
import com.team.domain.usecase.interestcategory.UpdateMyCategoriesUseCase
import com.team.domain.usecase.profile.GetMyProfileUseCase
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.TestDispatcherRule
import com.team.presentation.editcategories.state.MyCategoriesState
import com.team.presentation.editcategories.state.MyCategoriesUpdateState
import com.team.presentation.editcategories.testdoubles.myProfileTestData
import com.team.presentation.fake.FakeDataStoreManager
import com.team.presentation.home.testdoubles.categoriesTestData
import com.team.presentation.home.testdoubles.myCategoriesTestData
import com.team.presentation.util.uitext.asUiText
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class EditMyCategoriesViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var editMyCategoriesViewModel: EditMyCategoriesViewModel
    private val getMyProfileUseCase: GetMyProfileUseCase = mockk()
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val updateMyCategoriesUseCase: UpdateMyCategoriesUseCase = mockk()
    private val getSpeechBubbleCountUseCase: GetSpeechBubbleCountUseCase = mockk()
    private val incrementSpeechBubbleCountUseCase: IncrementSpeechBubbleCountUseCase = mockk()
    private lateinit var dataStoreManager: DataStoreManager

    private val context: Context = mockk()

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()

        every { getCategoriesUseCase() } returns flowOf(categoriesTestData)

        every { ErrorType.Exception.IO.asUiText().asString(context) } returns "IO Exception"
    }

    @Test
    fun `말풍선 힌트 횟수 초과`() = runTest {
        coEvery { getMyProfileUseCase() } returns flowOf(Result.Success(myProfileTestData))

        // Given
        every {
            getSpeechBubbleCountUseCase()
        } returns flowOf(0)

        // When
        editMyCategoriesViewModel = EditMyCategoriesViewModel(
            UnconfinedTestDispatcher(),
            getMyProfileUseCase,
            getCategoriesUseCase,
            updateMyCategoriesUseCase,
            getSpeechBubbleCountUseCase,
            incrementSpeechBubbleCountUseCase
        )

        advanceUntilIdle()

        // Then
        val speechBubbleState = editMyCategoriesViewModel.speechBubbleState.first()

        assert(!speechBubbleState)
    }

    @Test
    fun `말풍선 힌트 횟수 미초과`() = runTest {
        coEvery { getMyProfileUseCase() } returns flowOf(Result.Success(myProfileTestData))

        // Given
        every {
            getSpeechBubbleCountUseCase()
        } returns flowOf(null)
        coEvery {
            incrementSpeechBubbleCountUseCase()
        } just Runs

        // When
        editMyCategoriesViewModel = EditMyCategoriesViewModel(
            UnconfinedTestDispatcher(),
            getMyProfileUseCase,
            getCategoriesUseCase,
            updateMyCategoriesUseCase,
            getSpeechBubbleCountUseCase,
            incrementSpeechBubbleCountUseCase
        )

        advanceUntilIdle()

        // Then
        val speechBubbleState = editMyCategoriesViewModel.speechBubbleState.first()

        assert(speechBubbleState)
    }

    @Test
    fun `카테고리 가져오기 (나의 관심 카테고리 및 제외한 카테고리) - 성공 시`() = runTest {
        every {
            getSpeechBubbleCountUseCase()
        } returns flowOf(null)
        coEvery {
            incrementSpeechBubbleCountUseCase()
        } just Runs

        // Given
        coEvery { getMyProfileUseCase() } returns flowOf(Result.Success(myProfileTestData.copy(categories = myCategoriesTestData.map { it.id })))

        val expectedMyCategories = myCategoriesTestData.mapNotNull { id ->
            categoriesTestData.find { it.id == id.id }
        }
        val expectedExclusiveCategories = categoriesTestData.filter { !myCategoriesTestData.map { m -> m.id }.contains(it.id) }

        var myCategoriesState: MyCategoriesState? = null
        val job = launch {
            editMyCategoriesViewModel.myCategoriesState.collectLatest {
                myCategoriesState = it
            }
        }

        // When
        editMyCategoriesViewModel = EditMyCategoriesViewModel(
            UnconfinedTestDispatcher(),
            getMyProfileUseCase,
            getCategoriesUseCase,
            updateMyCategoriesUseCase,
            getSpeechBubbleCountUseCase,
            incrementSpeechBubbleCountUseCase
        )

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val actualMyCategories = myCategoriesState?.myCategories
        val actualExclusiveCategories = myCategoriesState?.exclusiveCategories

        assertEquals(expectedMyCategories, actualMyCategories)
        assertEquals(expectedExclusiveCategories, actualExclusiveCategories)
    }

    @Test
    fun `카테고리 가져오기 (나의 관심 카테고리 및 제외한 카테고리) - 예외 발생 시`() = runTest {
        every {
            getSpeechBubbleCountUseCase()
        } returns flowOf(null)
        coEvery {
            incrementSpeechBubbleCountUseCase()
        } just Runs

        // Given
        val expectedException = CancellationException()
        coEvery { getMyProfileUseCase() } throws expectedException

        var myCategoriesState: MyCategoriesState? = null
        val job = launch {
            editMyCategoriesViewModel.myCategoriesState.collectLatest {
                myCategoriesState = it
            }
        }

        // When
        editMyCategoriesViewModel = EditMyCategoriesViewModel(
            UnconfinedTestDispatcher(),
            getMyProfileUseCase,
            getCategoriesUseCase,
            updateMyCategoriesUseCase,
            getSpeechBubbleCountUseCase,
            incrementSpeechBubbleCountUseCase
        )

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val actualException = myCategoriesState?.error

        assertEquals(
            expectedException.localizedMessage
                ?: ErrorType.Exception.IO.asUiText().asString(context),
            actualException?.asString(context)
        )
    }

    @Test
    fun `나의 관심 카테고리 업데이트 - 성공 시`() = runTest {
        coEvery { getMyProfileUseCase() } returns flowOf(Result.Success(myProfileTestData.copy(categories = myCategoriesTestData.map { it.id })))
        every {
            getSpeechBubbleCountUseCase()
        } returns flowOf(null)
        coEvery {
            incrementSpeechBubbleCountUseCase()
        } just Runs

        // Given
        val expectedMyCategories = listOf(
            Category(0, "0"), Category(1, "1"), Category(2, "2")
        )
        every {
            updateMyCategoriesUseCase(expectedMyCategories)
        } returns flowOf(Result.Success(true))

        editMyCategoriesViewModel = EditMyCategoriesViewModel(
            UnconfinedTestDispatcher(),
            getMyProfileUseCase,
            getCategoriesUseCase,
            updateMyCategoriesUseCase,
            getSpeechBubbleCountUseCase,
            incrementSpeechBubbleCountUseCase
        )

        var myCategoriesUpdateState: MyCategoriesUpdateState? = null
        val job = launch {
            editMyCategoriesViewModel.myCategoriesUpdateState.collectLatest {
                myCategoriesUpdateState = it
            }
        }

        // When
        editMyCategoriesViewModel.updateMyCategories(expectedMyCategories)

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        assertNotNull(myCategoriesUpdateState)
        assert(myCategoriesUpdateState!!.success)
    }
}