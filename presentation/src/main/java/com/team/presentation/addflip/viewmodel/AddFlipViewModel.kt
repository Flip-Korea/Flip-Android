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
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.addflip.AddFlipUiEvent
import com.team.presentation.addflip.state.AddPostState
import com.team.presentation.addflip.state.CategoriesState
import com.team.presentation.common.snackbar.SnackbarAction
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
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
): ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoriesState())
    val categoriesState = _categoriesState.asStateFlow()

    private val _selectedCategory: MutableStateFlow<Category?> = MutableStateFlow(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _addPostState = MutableStateFlow(AddPostState())
    val addPostState: StateFlow<AddPostState> = _addPostState.asStateFlow()

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
            _categoriesState.update { it.copy(
                error = e.localizedMessage?.let { msg ->
                    UiText.DynamicString(msg)
                } ?: ErrorType.Exception.EXCEPTION.asUiText()
            ) }
        }
    }

    fun onUiEvent(uiEvent: AddFlipUiEvent) {
        when (uiEvent) {
            is AddFlipUiEvent.OnSelectedCategoryChanged -> {
                _selectedCategory.update { uiEvent.category }
            }
            is AddFlipUiEvent.OnSavePost -> {
                onSavePost(uiEvent.title, uiEvent.content, uiEvent.selectedColor, uiEvent.tags)
            }
            is AddFlipUiEvent.OnSaveTempPost -> {
                onSaveTempPost(uiEvent.title, uiEvent.content, uiEvent.selectedColor, uiEvent.tags)
            }
        }
    }

    /**
     * Flip(Post) 글 등록(= 게시, = 저장)
     */
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
                    bgColorType = selectedColor,
                    tags = tags,
                    categoryId = selectedCategory.value?.id
                )
            }

            val validationResults = validationResultsDeferred.await()

            // 유효성 검사 결과에 에러가 1개 라도 있을 경우
            if (validationResults.any { it is ValidationResult.Error }) {
                _addPostState.update { it.copy(
                    loading = false,
                    error = validationResults
                        .filterIsInstance<ValidationResult.Error>()
                        .first().error.asUiText()
                ) }
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
                            val message = result.errorBody?.let { errorBody ->
                                UiText.DynamicString(errorBody.message)
                            } ?: result.error.asUiText()

                            showSnackbar(message)
//                            _addPostState.update { it.copy(
//                                loading = false,
//                                error = result.errorBody?.let { errorBody ->
//                                    UiText.DynamicString(errorBody.message)
//                                } ?: result.error.asUiText()
//                            ) }
                        }
                        is Result.Success -> {
                            _addPostState.update { it.copy(
                                loading = false,
                                postSave = true
                            ) }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

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
                _addPostState.update { it.copy(
                    loading = false,
                    error = validationResult.error.asUiText()
                ) }
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
                            _addPostState.update { it.copy(loading = true) }
                        }
                        is Result.Error -> {
                            _addPostState.update { it.copy(
                                loading = false,
                                error = result.errorBody?.let { errorBody ->
                                    UiText.DynamicString(errorBody.message)
                                } ?: result.error.asUiText()
                            ) }
                        }
                        is Result.Success -> {
                            _addPostState.update { it.copy(
                                loading = false,
                                tempPostSave = true
                            ) }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun resetErrorState() {
        viewModelScope.launch {
            _addPostState.update { it.copy(error = UiText.DynamicString("")) }
        }
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