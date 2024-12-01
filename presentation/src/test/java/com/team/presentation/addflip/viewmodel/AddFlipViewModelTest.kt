package com.team.presentation.addflip.viewmodel

import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.util.ErrorBody
import com.team.presentation.addflip.helper.AddFlipViewModelTestHelper
import com.team.presentation.addflip.state.AddFlipContract
import com.team.presentation.addflip.state.PostSaveState
import com.team.presentation.addflip.testdoubles.categoriesTestData
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.util.BaseTest
import com.team.presentation.util.ViewModelTestTemplate
import com.team.presentation.util.uitext.UiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddFlipViewModelTest : BaseTest() {
    private val testHelper = AddFlipViewModelTestHelper()
    private val viewModelTestTemplate = ViewModelTestTemplate()

    @Before
    fun setUp() {
        testHelper.mockValidatePostUseCase(TITLE, CONTENTS, CATEGORY)
        testHelper.mockAddPostUseCase(TITLE, CONTENTS, BG_COLOR_TYPE, CATEGORY)
    }

    @Test
    fun `카테고리 가져오기`() = runTest {
        val viewModel = testHelper.createViewModel()
        val categoriesState = viewModel.currentUiState as AddFlipContract.UiState.Content
        assertEquals(categoriesState.categories, categoriesTestData)
    }

    @Test
    fun `Flip(Post) 등록하기 성공 (onSavePost())`() {
        var postSaveState: PostSaveState? = null
        val viewModel = testHelper.createViewModel()

        viewModelTestTemplate.launch {
            viewModel.uiState
                .filterIsInstance<AddFlipContract.UiState.Content>()
                .collectLatest { uiState ->
                    postSaveState = uiState.postSaveState
                }
        }

        viewModelTestTemplate.`when` {
            viewModel.processEvent(
                AddFlipContract.UiEvent.SavePost(
                    TITLE, CONTENTS, BG_COLOR_TYPE, CATEGORY
                )
            )
        }

        viewModelTestTemplate.then {
            assertTrue(postSaveState!!.postSave)
        }
    }

    @Test
    fun `Flip(Post) 등록하기 실패 (onSavePost())`() {
        var expectedError: UiText? = null
        val viewModel = testHelper.createViewModel()
        testHelper.mockAddPostUseCase(
            TITLE,
            CONTENTS,
            BG_COLOR_TYPE,
            CATEGORY,
            ERROR_BODY
        )

        viewModelTestTemplate.launch {
            SnackbarController.events.collectLatest {
                expectedError = it.message
            }
        }

        viewModelTestTemplate.`when` {
            viewModel.processEvent(
                AddFlipContract.UiEvent.SavePost(
                    TITLE, CONTENTS, BG_COLOR_TYPE, CATEGORY
                )
            )
        }

        viewModelTestTemplate.then {
            assertEquals(
                expectedError,
                UiText.DynamicString(ERROR_BODY.message)
            )
        }
    }

    // 임시 저장 기능은 버전 2로 미뤄졌음...

    companion object {
        private const val TITLE = "title"
        private val CONTENTS = listOf("content")
        private val BG_COLOR_TYPE = BackgroundColorType.DEFAULT
        private val CATEGORY = Category(1, "A")
        private val ERROR_BODY = ErrorBody("", emptyList(), "error")
    }
}
