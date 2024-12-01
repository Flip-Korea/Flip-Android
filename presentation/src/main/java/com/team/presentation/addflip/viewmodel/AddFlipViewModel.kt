package com.team.presentation.addflip.viewmodel

import androidx.lifecycle.viewModelScope
import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.post.AddPostUseCase
import com.team.domain.usecase.post.ValidatePostUseCase
import com.team.domain.usecase.temppost.AddTempPostUseCase
import com.team.domain.usecase.temppost.ValidateSafeSaveUseCase
import com.team.domain.usecase.temppost.ValidateTempPostUseCase
import com.team.domain.util.Result
import com.team.domain.util.SafeSaveResult
import com.team.domain.util.SuccessType
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.addflip.state.AddFlipContract
import com.team.presentation.common.snackbar.SnackbarAction
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.common.state.ModalState
import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import com.team.presentation.util.uitext.errorBodyFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFlipViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val addTempPostUseCase: AddTempPostUseCase,
    private val validatePostUseCase: ValidatePostUseCase,
    private val validateTempPostUseCase: ValidateTempPostUseCase,
    private val validateSafeSaveUseCase: ValidateSafeSaveUseCase,
) : FlipBaseViewModel<AddFlipContract.UiState, AddFlipContract.UiEvent, AddFlipContract.UiEffect>() {

    init {
        viewModelScope.launch {
            fetchCategories()
        }
    }

    override fun createInitialState(): AddFlipContract.UiState {
        return AddFlipContract.UiState.Content()
    }

    override suspend fun handleEvent(event: AddFlipContract.UiEvent) {
        when (event) {
            is AddFlipContract.UiEvent.OnTitleChanged -> onTitleChanged(event.title)
            is AddFlipContract.UiEvent.OnContentsChanged -> onContentsChanged(event.contents)
            is AddFlipContract.UiEvent.OnBackgroundColorChanged -> onBackgroundChanged(event.bgColorType)
            is AddFlipContract.UiEvent.OnCategoryChanged -> onCategoryChanged(event.category)
            is AddFlipContract.UiEvent.OnPageDelete -> showPageDeleteWarningModal(event.complete)
            is AddFlipContract.UiEvent.SaveTempPost -> {
                saveTempPost(event.title, event.contents, event.bgColorType, event.category)
            }
            is AddFlipContract.UiEvent.SavePost -> {
                savePost(event.title, event.contents, event.bgColorType, event.category)
            }
            AddFlipContract.UiEvent.SafeNavigateBack -> navigateBackToSafeSave()
            AddFlipContract.UiEvent.NavigateBack -> {
                sendEffect { AddFlipContract.UiEffect.NavigateBack(true) }
            }
        }
    }

    private fun savePost(
        title: String,
        contents: List<String>,
        bgColorType: BackgroundColorType,
        category: Category?,
    ) {
        viewModelScope.launch {
            val contentState = getContentState()
            if (validationPostForSave(title, contents, category)) {
                addPostUseCase(
                    title = title,
                    content = contents,
                    bgColorType = bgColorType,
                    categoryId = category!!.id
                ).onEach { result ->
                    when (result) {
                        Result.Loading -> {
                            val updatedPostSaveState =
                                contentState.postSaveState.copy(loading = true)
                            updateState {
                                contentState.copy(postSaveState = updatedPostSaveState)
                            }
                        }

                        is Result.Error -> {
                            val updatedPostSaveState =
                                contentState.postSaveState.copy(loading = false)
                            updateState {
                                contentState.copy(postSaveState = updatedPostSaveState)
                            }
                            showSnackbar(errorBodyFirst(result.errorBody, result.error))
                        }

                        is Result.Success -> {
                            val updatedPostSaveState =
                                contentState.postSaveState.copy(postSave = true, loading = false)
                            updateState {
                                contentState.copy(postSaveState = updatedPostSaveState)
                            }
                            showSnackbar(SuccessType.Post.SAVE.asUiText())
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun validationPostForSave(
        title: String,
        contents: List<String>,
        category: Category?
    ): Boolean {
        val validationResults = validatePostUseCase(title, contents, category)
        var isValid = true
        viewModelScope.launch {
            for (i in validationResults.indices) {
                val result = validationResults[i]
                if (result is ValidationResult.Error) {
                    showSnackbar(result.error.asUiText())
                    isValid = false
                    break
                }
            }
        }
        return isValid
    }

    private fun saveTempPost(
        title: String,
        contents: List<String>,
        bgColorType: BackgroundColorType,
        category: Category?
    ) {
        viewModelScope.launch {
            val contentState = getContentState()
            val categoryId = category?.id
            val validationResult = validationTempPostForSave(title, contents)
            if (validationResult) {
                addTempPostUseCase(
                    title = title,
                    content = contents,
                    bgColorType = bgColorType,
                    categoryId = categoryId
                ).onEach { result ->
                    when (result) {
                        is Result.Error -> {
                            val updatedPostSaveState =
                                contentState.postSaveState.copy(loading = false)
                            updateState {
                                contentState.copy(postSaveState = updatedPostSaveState)
                            }
                            showSnackbar(errorBodyFirst(result.errorBody, result.error))
                        }

                        Result.Loading -> {
                            val updatedAddTempPostState =
                                contentState.postSaveState.copy(loading = true)
                            updateState {
                                contentState.copy(postSaveState = updatedAddTempPostState)
                            }
                        }

                        is Result.Success -> {
                            val updatedAddTempPostState =
                                contentState.postSaveState.copy(
                                    tempPostSave = true,
                                    loading = false
                                )
                            updateState {
                                contentState.copy(postSaveState = updatedAddTempPostState)
                            }
                            showSnackbar(SuccessType.TempPost.SAVE.asUiText())
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun validationTempPostForSave(title: String, contents: List<String>): Boolean {
        return when (val validationResult = validateTempPostUseCase(title, contents)) {
            is ValidationResult.Error -> {
                viewModelScope.launch {
                    showSnackbar(message = validationResult.error.asUiText())
                }
                false
            }

            ValidationResult.Success -> true
        }
    }

    private fun showPageDeleteWarningModal(complete: Boolean) {
        if (complete) {
            sendEffect { AddFlipContract.UiEffect.ShowPageDeleteWarningModal(ModalState.Hide) }
            return
        }
        sendEffect { AddFlipContract.UiEffect.ShowPageDeleteWarningModal(ModalState.Show) }
    }

    private fun navigateBackToSafeSave() {
        val contentState = getContentState()
        val title = contentState.newPostState.title
        val contents = contentState.newPostState.contents
        when (validateSafeSaveUseCase(title, contents)) {
            SafeSaveResult.CanSave -> {
                sendEffect { AddFlipContract.UiEffect.NavigateBack(false) }
            }

            SafeSaveResult.Discard -> {
                sendEffect { AddFlipContract.UiEffect.NavigateBack(true) }
            }
        }
    }

    private fun onTitleChanged(title: String) {
        val contentState = getContentState()
        val updatedNewPostState = contentState.newPostState.copy(title = title)
        updateState { contentState.copy(newPostState = updatedNewPostState) }
    }

    private fun onContentsChanged(contents: List<String>) {
        val contentState = getContentState()
        val updatedNewPostState = contentState.newPostState.copy(contents = contents)
        updateState { contentState.copy(newPostState = updatedNewPostState) }
    }

    private fun onBackgroundChanged(bgColorType: BackgroundColorType) {
        val contentState = getContentState()
        val updatedNewPostState = contentState.newPostState.copy(bgColorType = bgColorType)
        updateState { contentState.copy(newPostState = updatedNewPostState) }
    }

    private fun onCategoryChanged(category: Category) {
        val contentState = getContentState()
        val updatedNewPostState = contentState.newPostState.copy(category = category)
        updateState { contentState.copy(newPostState = updatedNewPostState) }
    }

    private suspend fun fetchCategories() {
        val categories = getCategoriesUseCase().first()
        val contentState = getContentState()
        updateState {
            contentState.copy(categories = categories)
        }
    }

    /** 현재 상태 값을 기준으로 Content 상태 데이터를 추출 */
    private fun getContentState(): AddFlipContract.UiState.Content {
        val currentState = currentUiState
        if (currentState is AddFlipContract.UiState.Content) {
            return currentState
        }
        return AddFlipContract.UiState.Content()
    }

    private suspend fun showSnackbar(message: UiText, action: SnackbarAction? = null) {
        SnackbarController.sendEvent(event = SnackbarEvent(message = message, action = action))
    }
}