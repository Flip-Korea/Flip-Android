package com.team.presentation.addflip.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.post.AddPostUseCase
import com.team.domain.usecase.post.ValidatePostUseCase
import com.team.domain.usecase.profile.GetCurrentProfileIdUseCase
import com.team.domain.usecase.temppost.AddTempPostUseCase
import com.team.domain.usecase.temppost.ValidateTempPostUseCase
import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.TestDispatcherRule
import com.team.presentation.addflip.AddFlipUiEvent
import com.team.presentation.addflip.state.AddPostState
import com.team.presentation.addflip.testdoubles.categoriesTestData
import com.team.presentation.util.uitext.UiText
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AddFlipViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var addFlipViewModel: AddFlipViewModel
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val getCurrentProfileIdUseCase: GetCurrentProfileIdUseCase = mockk()
    private val addPostUseCase: AddPostUseCase = mockk()
    private val addTempPostUseCase: AddTempPostUseCase = mockk()
    private val validatePostUseCase: ValidatePostUseCase = mockk()
    private val validateTempPostUseCase: ValidateTempPostUseCase = mockk()

    private val title = "title"
    private val content = listOf("content")
    private val bgColorType = BackgroundColorType.DEFAULT
    private val tags = emptyList<String>()
    private val categoryId = 2

    @Before
    fun setUp() {
        every { getCategoriesUseCase.invoke() } returns flowOf(categoriesTestData)
    }

    @Test
    fun `카테고리 가져오기`() = runTest {
        addFlipViewModel = AddFlipViewModel(
            getCurrentProfileIdUseCase = getCurrentProfileIdUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            addPostUseCases = addPostUseCase,
            addTempPostUseCase = addTempPostUseCase,
            validatePostUseCase = validatePostUseCase,
            validateTempPostUseCase = validateTempPostUseCase,
            ioDispatcher = UnconfinedTestDispatcher()
        )

        val categoriesState = addFlipViewModel.categoriesState.first()

        assertEquals(categoriesState.categories,  categoriesTestData)
    }

    @Test
    fun `Flip(Post) 등록하기 성공 (onSavePost())`() = runTest {
        // Given
        val selectedColor = BackgroundColorType.DEFAULT
        val selectedCategory = Category(2, "2")
        every {
            validatePostUseCase(title, content, bgColorType, tags, categoryId)
        } returns listOf(ValidationResult.Success)

        every {
            addPostUseCase(
                title = title,
                content = content,
                bgColorType = selectedColor,
                tags = tags,
                categoryId = selectedCategory.id
            )
        } returns flowOf(Result.Success(true))

        addFlipViewModel = AddFlipViewModel(
            UnconfinedTestDispatcher(),
            getCurrentProfileIdUseCase,
            getCategoriesUseCase,
            addPostUseCase,
            addTempPostUseCase,
            validatePostUseCase, validateTempPostUseCase
        )

        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSelectedCategoryChanged(selectedCategory))

        var addPostState: AddPostState? = null
        val job = launch {
            addFlipViewModel.addPostState.collectLatest {
                addPostState = it
            }
        }

        // When
        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSavePost(title,content, selectedColor, tags))

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val result = addPostState?.postSave

        assertNotNull(result)
        assert(result!!)
    }

    @Test
    fun `Flip(Post) 등록하기 실패 (onSavePost())`() = runTest {
        // Given
        val expectedErrorBody = ErrorBody("", emptyList(), "error")
        val selectedColor = BackgroundColorType.DEFAULT
        val selectedCategory = Category(2, "2")
        every {
            validatePostUseCase(title, content, bgColorType, tags, categoryId)
        } returns listOf(ValidationResult.Success)

        every {
            addPostUseCase(
                title = title,
                content = content,
                bgColorType = selectedColor,
                tags = tags,
                categoryId = selectedCategory.id
            )
        } returns flowOf(Result.Error(error = ErrorType.Network.BAD_REQUEST, errorBody = expectedErrorBody))

        addFlipViewModel = AddFlipViewModel(
            UnconfinedTestDispatcher(),
            getCurrentProfileIdUseCase,
            getCategoriesUseCase,
            addPostUseCase,
            addTempPostUseCase,
            validatePostUseCase, validateTempPostUseCase
        )

        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSelectedCategoryChanged(selectedCategory))

        var addPostState: AddPostState? = null
        val job = launch {
            addFlipViewModel.addPostState.collectLatest {
                addPostState = it
            }
        }

        // When
        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSavePost(title,content, selectedColor, tags))

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val result = addPostState?.postSave
        val actualError = addPostState?.error

        assertNotNull(result)
        assertNotNull(actualError)
        assert(!result!!)
        assertEquals(
            UiText.DynamicString(expectedErrorBody.message),
            actualError
        )
    }

    @Test
    fun `Flip(Post) 등록하기 실패(selectedCategory is null) (onSavePost())`() = runTest {
        // Given
        val selectedColor = BackgroundColorType.DEFAULT
        val selectedCategory = Category(2, "2")
        every {
            validatePostUseCase(title, content, bgColorType, tags, categoryId)
        } returns listOf(ValidationResult.Success)

        every {
            addPostUseCase(
                title = title,
                content = content,
                bgColorType = selectedColor,
                tags = tags,
                categoryId = selectedCategory.id
            )
        } returns flowOf(Result.Success(true))

        addFlipViewModel = AddFlipViewModel(
            UnconfinedTestDispatcher(),
            getCurrentProfileIdUseCase,
            getCategoriesUseCase,
            addPostUseCase,
            addTempPostUseCase,
            validatePostUseCase, validateTempPostUseCase
        )

        var addPostState: AddPostState? = null
        val job = launch {
            addFlipViewModel.addPostState.collectLatest {
                addPostState = it
            }
        }

        val expectedError = UiText.DynamicString("카테고리를 입력해주세요.")

        // When
        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSavePost(title,content, selectedColor, tags))

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val result = addPostState?.postSave
        val actualError = addPostState?.error

        assertNotNull(result)
        assert(!result!!)
        assertEquals(
            expectedError,
            actualError
        )
    }

    @Test
    fun `임시 Flip(Post) 저장 성공 (onSavePost())`() = runTest {
        // Given
        val selectedColor = BackgroundColorType.DEFAULT
        val selectedCategory = Category(2, "2")
        every {
            validateTempPostUseCase(title, content)
        } returns ValidationResult.Success

        every {
            addTempPostUseCase(
                title = title,
                content = content,
                bgColorType = selectedColor,
                tags = tags,
                categoryId = selectedCategory.id
            )
        } returns flowOf(Result.Success(true))

        addFlipViewModel = AddFlipViewModel(
            UnconfinedTestDispatcher(),
            getCurrentProfileIdUseCase,
            getCategoriesUseCase,
            addPostUseCase,
            addTempPostUseCase,
            validatePostUseCase, validateTempPostUseCase
        )

        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSelectedCategoryChanged(selectedCategory))

        var addPostState: AddPostState? = null
        val job = launch {
            addFlipViewModel.addPostState.collectLatest {
                addPostState = it
            }
        }

        // When
        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSaveTempPost(title,content, selectedColor, tags))

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val result = addPostState?.tempPostSave

        assertNotNull(result)
        assert(result!!)
    }

    @Test
    fun `임시 Flip(Post) 저장 실패 (onSavePost())`() = runTest {
        // Given
        val expectedErrorBody = ErrorBody("", emptyList(), "error")
        val selectedColor = BackgroundColorType.DEFAULT
        val selectedCategory = Category(2, "2")
        every {
            validateTempPostUseCase(title, content)
        } returns ValidationResult.Success

        every {
            addTempPostUseCase(
                title = title,
                content = content,
                bgColorType = selectedColor,
                tags = tags,
                categoryId = selectedCategory.id
            )
        } returns flowOf(Result.Error(error = ErrorType.Network.BAD_REQUEST, errorBody = expectedErrorBody))

        addFlipViewModel = AddFlipViewModel(
            UnconfinedTestDispatcher(),
            getCurrentProfileIdUseCase,
            getCategoriesUseCase,
            addPostUseCase,
            addTempPostUseCase,
            validatePostUseCase, validateTempPostUseCase
        )

        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSelectedCategoryChanged(selectedCategory))

        var addPostState: AddPostState? = null
        val job = launch {
            addFlipViewModel.addPostState.collectLatest {
                addPostState = it
            }
        }

        // When
        addFlipViewModel.onUiEvent(AddFlipUiEvent.OnSaveTempPost(title,content, selectedColor, tags))

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val result = addPostState?.tempPostSave
        val actualErrorBody = addPostState?.error

        assertNotNull(result)
        assert(!result!!)
        assertEquals(
            UiText.DynamicString(expectedErrorBody.message),
            actualErrorBody
        )
    }
}