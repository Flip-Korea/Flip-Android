package com.team.presentation.addflip.helper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.post.AddPostUseCase
import com.team.domain.usecase.post.ValidatePostUseCase
import com.team.domain.usecase.temppost.AddTempPostUseCase
import com.team.domain.usecase.temppost.ValidateSafeSaveUseCase
import com.team.domain.usecase.temppost.ValidateTempPostUseCase
import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.TestDispatcherRule
import com.team.presentation.addflip.testdoubles.categoriesTestData
import com.team.presentation.addflip.viewmodel.AddFlipViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule

@ExperimentalCoroutinesApi
class AddFlipViewModelTestHelper {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var addFlipViewModel: AddFlipViewModel
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val addPostUseCase: AddPostUseCase = mockk()
    private val addTempPostUseCase: AddTempPostUseCase = mockk()
    private val validatePostUseCase: ValidatePostUseCase = mockk()
    private val validateTempPostUseCase: ValidateTempPostUseCase = mockk()
    private val validateSafeSaveUseCase: ValidateSafeSaveUseCase = mockk()

    init {
        every { getCategoriesUseCase.invoke() } returns flowOf(categoriesTestData)
    }

    fun createViewModel(): AddFlipViewModel {
        addFlipViewModel =
            AddFlipViewModel(
                getCategoriesUseCase = getCategoriesUseCase,
                addPostUseCase = addPostUseCase,
                addTempPostUseCase = addTempPostUseCase,
                validatePostUseCase = validatePostUseCase,
                validateTempPostUseCase = validateTempPostUseCase,
                validateSafeSaveUseCase = validateSafeSaveUseCase,
            )
        return addFlipViewModel
    }

    fun mockAddPostUseCase(
        title: String,
        contents: List<String>,
        bgColorType: BackgroundColorType,
        category: Category,
        errorBody: ErrorBody? = null,
    ) {
        if (errorBody == null) {
            every {
                addPostUseCase(
                    title = title,
                    content = contents,
                    bgColorType = bgColorType,
                    categoryId = category.id,
                )
            } returns flowOf(Result.Success(true))
        } else {
            every {
                addPostUseCase(
                    title = title,
                    content = contents,
                    bgColorType = bgColorType,
                    categoryId = category.id,
                )
            } returns
                flowOf(
                    Result.Error(
                        error = ErrorType.Exception.EXCEPTION,
                        errorBody = errorBody,
                    ),
                )
        }
    }

    fun mockValidatePostUseCase(
        title: String,
        contents: List<String>,
        category: Category?,
        error: ValidationErrorType? = null,
    ) {
        if (error == null) {
            every {
                validatePostUseCase.invoke(title = title, content = contents, category = category)
            } returns listOf(ValidationResult.Success)
        } else {
            every {
                validatePostUseCase.invoke(title = title, content = contents, category = category)
            } returns listOf(ValidationResult.Error(error))
        }
    }
}
