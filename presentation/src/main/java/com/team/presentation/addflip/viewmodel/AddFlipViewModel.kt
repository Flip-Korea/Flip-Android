package com.team.presentation.addflip.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.category.Category
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.profile.GetCurrentProfileIdUseCase
import com.team.domain.util.ErrorType
import com.team.presentation.addflip.AddFlipUiEvent
import com.team.presentation.addflip.state.CategoriesState
import com.team.presentation.util.UiText
import com.team.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFlipViewModel @Inject constructor(
    private val getCurrentProfileIdUseCase: GetCurrentProfileIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
): ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoriesState())
    val categoriesState = _categoriesState.asStateFlow()

    private val _selectedCategory: MutableStateFlow<Category?> = MutableStateFlow(null)
    val selectedCategory = _selectedCategory.asStateFlow()

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
                //TODO 플립 등록
            }
            is AddFlipUiEvent.OnSaveTempPost -> {
                //TODO 플립 임시저장
            }
        }
    }
}