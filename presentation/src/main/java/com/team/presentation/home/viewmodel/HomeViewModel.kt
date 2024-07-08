package com.team.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.DataStoreManager
import com.team.domain.model.category.Category
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.home.GetPostsByTypeUseCase
import com.team.domain.usecase.interest_category.GetMyCategoriesUseCase
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.HomeUiEvent
import com.team.presentation.home.state.CategoryState
import com.team.presentation.home.state.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsByTypeUseCase: GetPostsByTypeUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMyCategoriesUseCase: GetMyCategoriesUseCase,
    private val dataStoreManager: DataStoreManager,
): ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoryState())
    val categoriesState = _categoriesState.asStateFlow()

    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()

    init {
        viewModelScope.launch {
            val allCategories = getAlignedCategories()
            _categoriesState.update {
                it.copy(
                    categories = allCategories,
                    splitSize = fixedCategories.size
                )
            }
        }
    }

    fun onFlipCardEvent(uiEvent: FlipCardUiEvent) {
        when (uiEvent) {
            FlipCardUiEvent.OnCommentClick -> { }
            FlipCardUiEvent.OnLikeClick -> { }
            FlipCardUiEvent.OnMoreClick -> { }
            FlipCardUiEvent.OnScrapClick -> { }
            FlipCardUiEvent.OnFlipCardClick -> { }
        }
    }

    fun onHomeUiEvent(uiEvent: HomeUiEvent) {
        when (uiEvent) {
            HomeUiEvent.OnNotificationClick -> { }
            HomeUiEvent.OnSearchClick -> { }
        }
    }

    private suspend fun getAlignedCategories(): List<Category> {
        val currentProfileId =
            dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()
        val categories = getCategoriesUseCase().first()
        val myCategories = getMyCategoriesUseCase(currentProfileId ?: "").first()
        val allCategories = fixedCategories + categories.sortedByDescending { cate ->
            myCategories.contains(cate.id)
        }
        return allCategories
    }
}

val fixedCategories = listOf(
    Category(100, "전체"),
    Category(101, "팔로잉"),
    Category(102, "인기 플립"),
)