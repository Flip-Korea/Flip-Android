package com.team.presentation.addflip.viewmodel

import androidx.lifecycle.viewModelScope
import com.team.domain.model.category.Category
import com.team.domain.type.BackgroundColorType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.post.AddPostUseCase
import com.team.domain.usecase.post.ValidatePostUseCase
import com.team.domain.usecase.temppost.AddTempPostUseCase
import com.team.domain.usecase.temppost.ValidateTempPostUseCase
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.addflip.state.AddFlipContract
import com.team.presentation.common.state.ModalState
import com.team.presentation.common.util.FlipBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFlipViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addPostUseCases: AddPostUseCase,
    private val addTempPostUseCase: AddTempPostUseCase,
    private val validatePostUseCase: ValidatePostUseCase,
    private val validateTempPostUseCase: ValidateTempPostUseCase,
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
            AddFlipContract.UiEvent.OnSafeSave -> validateTempPost()
            is AddFlipContract.UiEvent.OnPageDelete -> {
                if (event.complete) {
                    sendEffect { AddFlipContract.UiEffect.ShowPageDeleteWarningModal(ModalState.Hide) }
                    return
                }
                sendEffect { AddFlipContract.UiEffect.ShowPageDeleteWarningModal(ModalState.Show) }
            }

            AddFlipContract.UiEvent.SaveTempPost -> TODO()
            AddFlipContract.UiEvent.SavePost -> TODO()
        }
    }

    private fun validateTempPost() {
        extractContentState { content ->
            val title = content.newPostState.title
            val contents = content.newPostState.contents
            viewModelScope.launch {
                when (validateTempPostUseCase(title, contents)) {
                    is ValidationResult.Error -> passModal()
                    ValidationResult.Success -> showModal()
                }
            }
        }
    }

    private fun onTitleChanged(title: String) {
        extractContentState { content ->
            val updatedNewPostState = content.newPostState.copy(title = title)
            updateState { content.copy(newPostState = updatedNewPostState) }
        }
    }

    private fun onContentsChanged(contents: List<String>) {
        extractContentState { content ->
            val updatedNewPostState = content.newPostState.copy(contents = contents)
            updateState { content.copy(newPostState = updatedNewPostState) }
        }
    }

    private fun onBackgroundChanged(bgColorType: BackgroundColorType) {
        extractContentState { content ->
            val updatedNewPostState = content.newPostState.copy(bgColorType = bgColorType)
            updateState { content.copy(newPostState = updatedNewPostState) }
        }
    }

    private fun onCategoryChanged(category: Category) {
        extractContentState { content ->
            val updatedNewPostState = content.newPostState.copy(category = category)
            updateState { content.copy(newPostState = updatedNewPostState) }
        }
    }

    private suspend fun fetchCategories() {
        val categories = getCategoriesUseCase().first()
        extractContentState { content ->
            updateState {
                content.copy(categories = categories)
            }
        }
    }

    private fun extractContentState(block: (content: AddFlipContract.UiState.Content) -> Unit) {
        val currentState = currentUiState
        if (currentState is AddFlipContract.UiState.Content) {
            block(currentState)
        }
    }

    private fun showModal() {
        sendEffect { AddFlipContract.UiEffect.ShowTempPostWarningModal(ModalState.Show) }
    }

    private fun passModal() {
        sendEffect { AddFlipContract.UiEffect.ShowTempPostWarningModal(ModalState.Pass) }
    }

    private fun hideModal() {
        sendEffect { AddFlipContract.UiEffect.ShowTempPostWarningModal(ModalState.Hide) }
    }
}

private fun List<String>.add(newPageIndex: Int): List<String> =
    this.toMutableList().apply {
        add(newPageIndex, "")
    }

private fun List<String>.remove(currentPage: Int): List<String> =
    this.toMutableList().apply {
        removeAt(currentPage)
    }
