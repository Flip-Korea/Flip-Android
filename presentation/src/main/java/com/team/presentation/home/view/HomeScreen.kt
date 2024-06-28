package com.team.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.presentation.R
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.state.CategoryState
import com.team.presentation.home.state.PostState
import com.team.presentation.home.util.HomeScreenPaddingValues

/**
 * Flip의 메인 화면이자 홈 화면
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    categoryState: CategoryState,
    postState: PostState,
    flipCardUiEvent: (FlipCardUiEvent) -> Unit,
) {
    val density = LocalDensity.current

    val lazyListState = rememberLazyListState()

    var topBarHeightPx by remember { mutableFloatStateOf(0f) }
    var topBarHeightDp by remember { mutableStateOf(0.dp) }
    val topBarHeightOffsetPx = remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = topBarHeightOffsetPx.floatValue + delta
                topBarHeightOffsetPx.floatValue = newOffset.coerceIn(-topBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier.nestedScroll(nestedScrollConnection)
    ) {
        HomeTopBarWrapper(
            modifier = Modifier
                .onSizeChanged {
                    val padding = with(density) { HomeScreenPaddingValues.TopBarTopPadding.toPx() }
                    topBarHeightPx = it.height + padding
                    val heightDp = with(density) { it.height.toDp() }
                    topBarHeightDp = heightDp + HomeScreenPaddingValues.TopBarTopPadding
                },
            topBarHeightOffsetPx = topBarHeightOffsetPx.floatValue,
        ) {
            HomeTopBar(
                modifier = Modifier
                    .background(FlipTheme.colors.white)
                    .fillMaxWidth()
                    .padding(HomeScreenPaddingValues.TopBarPadding),
                logo = R.drawable.ic_logo_dark,
                onSearchClick = { },
                onSettingClick = { },
                onNotiClick = { }
            )
            HomeTab(
                modifier = Modifier
                    .background(FlipTheme.colors.white)
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                items = categoryState.categories,
                itemSplitSize = categoryState.splitSize,
                onItemClick = { }
            )
        }

        LazyColumn(
            modifier = modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .padding(HomeScreenPaddingValues.Horizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp, alignment = Alignment.Top),
            contentPadding = PaddingValues(bottom = 8.dp, top = topBarHeightDp),
            state = lazyListState
        ) {
            items(
                items = postState.posts,
                key = { post -> post.postId }
            ) { post ->
                HomeFlipCard(
                    modifier = Modifier.fillMaxWidth(),
                    post = post,
                    flipCardUiEvent = { flipCardUiEvent(it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    FlipAppTheme {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            categoryState = CategoryState(),
            postState = PostState().copy(
                posts = listOf(
                    Post(
                        postId = 0L,
                        profile = DisplayProfile(
                            nickname = "어스름늑대",
                            profileId = "90WXYZ6789A1B2C3",
                            photoUrl = ""
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
                        bgColorId = 1
                    )
                )
            ),
            flipCardUiEvent = { }
        )
    }
}