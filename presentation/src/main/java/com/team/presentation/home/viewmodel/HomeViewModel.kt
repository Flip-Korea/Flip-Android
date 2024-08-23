package com.team.presentation.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.data.di.DefaultDispatcher
import com.team.domain.model.category.fixedCategories
import com.team.domain.usecase.interestcategory.GetFilteredMyCategoriesUseCase
import com.team.domain.usecase.post.GetPostUseCases
import com.team.domain.usecase.profile.GetCurrentProfileIdUseCase
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.common.bottomsheet.block.BlockState
import com.team.presentation.common.bottomsheet.report.ReportState
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.HomeUiEvent
import com.team.presentation.home.state.PostState
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val getPostUseCases: GetPostUseCases,
    private val getCurrentProfileIdUseCase: GetCurrentProfileIdUseCase,
    private val getFilteredMyCategoriesUseCase: GetFilteredMyCategoriesUseCase
) : ViewModel() {

    val currentProfileID = getCurrentProfileIdUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ""
        )

    val filteredMyCategoriesState = getFilteredMyCategoriesUseCase()
        .flowOn(defaultDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()

    private var getPostsJob by mutableStateOf<Job?>(null)

    private var nextCursor by mutableStateOf<String?>(null)

    private val _reportState = MutableStateFlow(ReportState())
    val reportState = _reportState.asStateFlow()

    private val _blockState = MutableStateFlow(BlockState())
    val blockState = _blockState.asStateFlow()

    init {
        viewModelScope.launch {
            getPostsByCategory(fixedCategories[0].id)
        }
    }

    fun onFlipCardEvent(uiEvent: FlipCardUiEvent) {
        when (uiEvent) {
            FlipCardUiEvent.OnCommentClick -> {}
            FlipCardUiEvent.OnLikeClick -> {}
            FlipCardUiEvent.OnScrapClick -> {}
            FlipCardUiEvent.OnFlipCardClick -> {}
        }
    }

    fun onHomeUiEvent(uiEvent: HomeUiEvent) {
        when (uiEvent) {
            HomeUiEvent.OnNotificationClick -> {}
            HomeUiEvent.OnSearchClick -> {}
        }
    }

    /**
     * 카테고리 별로 Flip(Post)을 가져옴
     * @param categoryId 카테고리 ID
     * @see fixedCategories
     */
    fun getPostsByCategory(categoryId: Int) {
        if (getPostsJob != null) getPostsJob!!.cancel()

        when (categoryId) {
            /** 전체 */
            100 -> {
                getPostsJob = getAllPosts()
            }
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
    private fun getAllPosts(): Job {

        return viewModelScope.launch {

            try {

                getPostUseCases.getPostsUseCase(nextCursor).collect { result ->
                    _postState.update { state ->
                        when (result) {
                            Result.Loading -> state.copy(loading = true)
                            is Result.Error -> state.copy(
                                loading = false,
                                error = result.errorBody?.let { errorBody ->
                                    UiText.DynamicString(errorBody.message)
                                } ?: result.error.asUiText()
                            )
                            is Result.Success -> state.copy(
                                loading = false,
                                posts = result.data.posts
                            )
                        }
                    }
                }
            } catch (e: Exception) {

                _postState.update { it.copy(
                    loading = false,
                    error = e.localizedMessage?.let { message ->
                        UiText.DynamicString(message)
                    } ?: ErrorType.Exception.EXCEPTION.asUiText()
                ) }
            }
//            postUseCases.getPostsUseCase(nextCursor).onEach { result ->
//                when (result) {
//                    is Result.Success -> {
//                        _postState.update {
//                            it.copy(
//                                loading = false,
//                                posts = result.data.posts
//                            )
//                        }
//                    }
//
//                    is Result.Error -> {
//                        _postState.update {
//                            it.copy(
//                                loading = false,
//                                error = result.errorBody?.let { errorBody ->
//                                    UiText.DynamicString(errorBody.message)
//                                } ?: result.error.asUiText()
//                            )
//                        }
//                    }
//
//                    Result.Loading -> {
//                        _postState.update { it.copy(loading = true) }
//                    }
//                }
//            }.launchIn(viewModelScope)
        }
    }

    //TODO API 문서 확정되고 나서 개발 마저 진행
    fun onReport(
        /** ReportReq 를 위한 매개변수 */
//        reportType: ReportType,
//        reportId: String,
//        reporterId: String,
//        postId: Long? = null,
//        commentId: Long? = null
    ) {

    }

    //TODO API 문서 확정되고 나서 개발 마저 진행
    fun onBlock(
        /** BlockReq 를 위한 매개변수 */
//        profileId: String,
//        postId: Long?,
//        blockedId: String
    ) {

    }

    override fun onCleared() {
        getPostsJob?.cancel()
        super.onCleared()
    }
}