package com.team.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.HomeUiEvent
import com.team.presentation.home.state.CategoryState
import com.team.presentation.home.state.PostState
import com.team.presentation.home.util.HomeTabMockItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel: ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoryState())
    val categoriesState = _categoriesState.asStateFlow()

    private val fakePosts = mutableListOf<Post>()
    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()

    private val fixedSelectedCategoryIds = listOf(0, 1, 2)
    private val tempSelectedCategoryIds = listOf(9, 10, 11)

    init {
        //TODO 임시코드(viewModelScope 블록 내부), 반드시 수정 혹은 삭제할 것
        viewModelScope.launch {
            _categoriesState.update {
                it.copy(
                    categories = HomeTabMockItems.sortedByDescending { category ->
                        (tempSelectedCategoryIds + fixedSelectedCategoryIds).contains(category.id)
                    },
                    splitSize = fixedSelectedCategoryIds.size
                )
            }

            _postState.update { it.copy(loading = true) }
            repeat(15) {
                fakePosts.add(
                    Post(
                        postId = it.toLong(),
                        profile = DisplayProfile(
                            nickname = "어스름늑대",
                            profileId = "90WXYZ6789A1B2C3",
                            photoUrl = "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg"
                        ),
                        title = "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
                        content = "행정권은 대통령을 수반으로 하는 정부에\n" +
                                "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                                "의하여 법률에 의한 재판을 받을 권리를 가진다.행정권은 대통령을 수반으로 하는 정부에\n" +
                                "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                                "의하여 법률에 의한 재판을 받을 권리를 가진다.",
                        createdAt = "2024.01.24",
                        liked = false,
                        likeCnt = 78,
                        commentCnt = 21,
                        scraped = false,
                        bgColorId = Random.nextInt(0,5)
                    )
                )
            }
            _postState.update { it.copy(
                posts = fakePosts,
                loading = false
            ) }
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
}