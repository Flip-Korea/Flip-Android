package com.team.presentation.addflip.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.data.di.IODispatcher
import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.post.AddPostUseCase
import com.team.domain.usecase.post.ValidatePostUseCase
import com.team.domain.usecase.profile.GetCurrentProfileIdUseCase
import com.team.domain.usecase.temppost.AddTempPostUseCase
import com.team.domain.usecase.temppost.ValidateTempPostUseCase
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.SuccessType
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.addflip.AddFlipUiEvent
import com.team.presentation.addflip.state.AddPostState
import com.team.presentation.addflip.state.AddTempPostState
import com.team.presentation.addflip.state.CategoriesState
import com.team.presentation.addflip.state.NewPostState
import com.team.presentation.common.snackbar.SnackbarAction
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.common.state.ModalState
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import com.team.presentation.util.uitext.errorBodyFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFlipViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getCurrentProfileIdUseCase: GetCurrentProfileIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addPostUseCases: AddPostUseCase,
    private val addTempPostUseCase: AddTempPostUseCase,
    private val validatePostUseCase: ValidatePostUseCase,
    private val validateTempPostUseCase: ValidateTempPostUseCase,
) : ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoriesState())
    val categoriesState = _categoriesState.asStateFlow()

    /** 작성하고 있는 플립 */
    private val _newPostState = MutableStateFlow(NewPostState())
    val newPostState = _newPostState.asStateFlow()

    private val _selectedCategory: MutableStateFlow<Category?> = MutableStateFlow(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _addPostState = MutableStateFlow(AddPostState())
    val addPostState: StateFlow<AddPostState> = _addPostState.asStateFlow()

    private val _addTempPostState = MutableStateFlow(AddTempPostState())
    val addTempPostState: StateFlow<AddTempPostState> = _addTempPostState.asStateFlow()

    private val _modalState: MutableStateFlow<ModalState> = MutableStateFlow(ModalState.Idle)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchCategories()
        }
    }

    private suspend fun fetchCategories() {
        try {
            val categories = getCategoriesUseCase().first()
            _categoriesState.update { it.copy(categories = categories) }
        } catch (e: Exception) {
            _categoriesState.update {
                it.copy(
                    error = e.localizedMessage?.let { msg ->
                        UiText.DynamicString(msg)
                    } ?: ErrorType.Exception.EXCEPTION.asUiText()
                )
            }
        }
    }

    fun onUiEvent(uiEvent: AddFlipUiEvent) {
        when (uiEvent) {
            is AddFlipUiEvent.OnSelectedCategoryChanged -> {
                _selectedCategory.update { uiEvent.category }
            }

            is AddFlipUiEvent.OnSavePost -> {
                onSavePost(uiEvent.title, uiEvent.contents, uiEvent.selectedColor, uiEvent.tags)
            }

            is AddFlipUiEvent.OnSaveTempPost -> {
                onSaveTempPost(uiEvent.title, uiEvent.contents, uiEvent.selectedColor, uiEvent.tags)
            }

            is AddFlipUiEvent.OnSafeSave -> {
                onSafeSave(
                    title = uiEvent.title,
                    contents = uiEvent.contents,
                )
            }

            is AddFlipUiEvent.OnCategoryChanged -> {
                _newPostState.update { it.copy(category = uiEvent.category) }
            }

            is AddFlipUiEvent.OnTitleChanged -> {
                _newPostState.update { it.copy(title = uiEvent.title) }
            }

            is AddFlipUiEvent.OnContentsChanged -> {
                _newPostState.update { it.copy(contents = uiEvent.contents) }
            }

            is AddFlipUiEvent.OnBackgroundColorChanged -> {
                _newPostState.update { it.copy(bgColorType = uiEvent.bgColorType) }
            }

            is AddFlipUiEvent.OnTagsChanged -> {
                _newPostState.update { it.copy(tags = uiEvent.tags) }
            }
        }
    }

    /** Flip(Post) 글 등록(= 게시, = 저장) */
    private fun onSavePost(
        title: String,
        content: List<String>,
        selectedColor: BackgroundColorType,
        tags: List<String>
    ) {
        viewModelScope.launch {
            if (selectedCategory.value == null) {
                _addPostState.update { it.copy(error = UiText.DynamicString("카테고리를 입력해주세요.")) }
                return@launch
            }

            _addPostState.update { it.copy(loading = true) }

            // 유효성 검사
            val validationResultsDeferred = async(ioDispatcher) {
                validatePostUseCase(
                    title = title,
                    content = content,
                    tags = tags,
                )
            }

            val validationResults = validationResultsDeferred.await()

            // 유효성 검사 결과에 에러가 1개 라도 있을 경우
            if (validationResults.any { it is ValidationResult.Error }) {
                _addPostState.update {
                    it.copy(
                        loading = false,
                        error = validationResults
                            .filterIsInstance<ValidationResult.Error>()
                            .first().error.asUiText()
                    )
                }
            } else {
                addPostUseCases(
                    title = title,
                    content = content,
                    bgColorType = selectedColor,
                    tags = tags,
                    categoryId = selectedCategory.value!!.id
                ).onEach { result ->
                    when (result) {
                        Result.Loading -> {
                            _addPostState.update { it.copy(loading = true) }
                        }

                        is Result.Error -> {
                            val message = errorBodyFirst(
                                errorBody = result.errorBody,
                                error = result.error
                            )

                            showSnackbar(message)
//                            _addPostState.update { it.copy(
//                                loading = false,
//                                error = message
//                            ) }
                        }

                        is Result.Success -> {
                            _addPostState.update {
                                it.copy(
                                    loading = false,
                                    postSave = true
                                )
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    /** Flip(Post) 임시 글 등록(= 저장) */
    private fun onSaveTempPost(
        title: String,
        content: List<String>,
        selectedColor: BackgroundColorType,
        tags: List<String>
    ) {
        viewModelScope.launch {
            // 유효성 검사
            val validationResultDeferred = async(ioDispatcher) {
                validateTempPostUseCase(title, content)
            }
            val validationResult = validationResultDeferred.await()
            if (validationResult is ValidationResult.Error) {
                _addTempPostState.update {
                    it.copy(
                        loading = false,
                        error = validationResult.error.asUiText()
                    )
                }
            } else {
                addTempPostUseCase(
                    title = title,
                    content = content,
                    bgColorType = selectedColor,
                    tags = tags,
                    categoryId = selectedCategory.value?.id
                ).onEach { result ->
                    when (result) {
                        Result.Loading -> {
                            _addTempPostState.update { it.copy(loading = true) }
                        }

                        is Result.Error -> {
                            _addTempPostState.update {
                                it.copy(
                                    loading = false,
                                    error = errorBodyFirst(
                                        errorBody = result.errorBody,
                                        error = result.error
                                    )
                                )
                            }
                        }

                        is Result.Success -> {
                            _addTempPostState.update {
                                it.copy(
                                    loading = false,
                                    tempPostSave = true
                                )
                            }
                            showSnackbar(message = SuccessType.TempPost.SAVE.asUiText())
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    /**
     * 뒤로가기 감지 시 작성된 내용이 있다면 (임시저장)경고모달 표시
     *
     * 기능 정의서[RQ-0038] 요약
     * 제목(title)이나 본문(content) 중 하나 라도 입력 했다면 경고모달 표시
     */
    private fun onSafeSave(
        title: String = "",
        contents: List<String> = emptyList(),
    ) {
        when (validateTempPostUseCase(title, contents)) {
            is ValidationResult.Error -> {
                viewModelScope.launch { displayModal(false) }
            }
            ValidationResult.Success -> { displayModal(true) }
        }
    }

    private fun displayModal(showed: Boolean) {
        _modalState.update { ModalState.Display(showed) }
    }

    fun hideModal() {
        _modalState.update { ModalState.Hide }
    }

    private suspend fun showSnackbar(
        message: UiText,
        action: SnackbarAction? = null
    ) {
        SnackbarController.sendEvent(
            event = SnackbarEvent(
                message = message,
                action = action
            )
        )
    }
}