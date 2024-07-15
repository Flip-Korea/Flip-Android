package com.team.presentation.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.DataStoreManager
import com.team.domain.model.category.Category
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.interestcategory.GetMyCategoriesUseCase
import com.team.domain.usecase.post.PostUseCases
import com.team.domain.util.Result
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.HomeUiEvent
import com.team.presentation.home.state.CategoryState
import com.team.presentation.home.state.PostState
import com.team.presentation.util.UiText
import com.team.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMyCategoriesUseCase: GetMyCategoriesUseCase,
    private val dataStoreManager: DataStoreManager,
): ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoryState())
    val categoriesState = _categoriesState.asStateFlow()

    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()

    private var getPostsJob by mutableStateOf<Job?>(null)

    private var nextCursor by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            val allCategories = getAlignedCategories()
            _categoriesState.update {
                it.copy(
                    categories = allCategories,
                    splitSize = fixedCategories.size
                )
            }

            getPostsByCategory(fixedCategories[0].id)
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

    /**
     * 모든 카테고리를 가져 오고 나의 관심 카테고리 순으로 정렬 한다.
     *
     * 결과: 고정 카테고리 + 나의 관심 카테고리 + 나머지 카테고리
     */
    suspend fun getAlignedCategories(): List<Category> {
        val currentProfileId =
            dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()
        val categories = getCategoriesUseCase().first()
        val myCategories = getMyCategoriesUseCase(currentProfileId ?: "").first()
        val allCategories = fixedCategories + categories.sortedByDescending { cate ->
            myCategories.contains(cate.id)
        }
        return allCategories
    }

    /**
     * 카테고리 별로 Flip(Post)을 가져옴
     * @param categoryId 카테고리 ID
     * @see fixedCategories
     */
    fun getPostsByCategory(categoryId: Int) {
        _postState.update { it.copy(loading = true) }

        if (getPostsJob != null) getPostsJob!!.cancel()

        when(categoryId) {
            /** 전체 */
            100 -> { getPostsJob = getAllPosts() }
            /** 팔로잉 */
            101 -> {}
            /** 인기 플립 */
            120 -> {}
            else -> {}
        }
    }

    /**
     * 모든 Flip(Post)을 네트워크에서 가져온다.
     *
     * (페이지네이션을 통해서 가져옴)
     */
    fun getAllPosts(): Job {
         return viewModelScope.launch {
             _postState.update { it.copy(loading = true) }

             postUseCases.getPostsUseCase(nextCursor).onEach { result ->
                 when(result) {
                     is Result.Success -> {
                         _postState.update { it.copy(
                             loading = false,
                             posts = result.data.posts
                         ) }
                     }
                     is Result.Error -> {
                         _postState.update { it.copy(
                             loading = false,
                             error = result.errorBody?.let { errorBody ->
                                 UiText.DynamicString(errorBody.message)
                             } ?: result.error.asUiText()
                         ) }
                     }
                     Result.Loading -> {
                         _postState.update { it.copy(loading = true) }
                     }
                 }
             }.launchIn(viewModelScope)
        }
    }

    /**
     * 캐시된 Flip(Post)을 가져온다.
     */
    fun getCachedPosts(): Job {
        return viewModelScope.launch {  }
    }

    override fun onCleared() {
        getPostsJob?.cancel()
        super.onCleared()
    }
}

/**
 * 고정 카테고리
 * 1. id(100): 전체
 * 2. id(101): 팔로잉
 * 3. id(102): 인기 플립
 */
val fixedCategories = listOf(
    Category(100, "전체"),
    Category(101, "팔로잉"),
    Category(102, "인기 플립"),
)