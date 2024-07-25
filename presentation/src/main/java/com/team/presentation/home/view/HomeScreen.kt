package com.team.presentation.home.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.designsystem.component.skeleton.HomeSkeletonScreen
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.presentation.R
import com.team.presentation.common.bottomsheet.ReportAndBlockUiEvent
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.HomeUiEvent
import com.team.presentation.home.state.CategoryState
import com.team.presentation.home.state.PostState
import com.team.presentation.home.util.HomeScreenPaddingValues
import kotlinx.coroutines.delay
import kotlin.math.abs

/**
 * Flip의 메인 화면이자 홈 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    categoryState: CategoryState,
    postState: PostState,
    onSettingClick: () -> Unit,
    flipCardUiEvent: (FlipCardUiEvent) -> Unit,
    reportAndBlockUiEvent: (ReportAndBlockUiEvent) -> Unit,
    homeUiEvent: (HomeUiEvent) -> Unit,
) {

    val density = LocalDensity.current

    val lazyListState = rememberLazyListState()

    /** TopBar Animation By Scrolled */
    var topBarHeightPx by remember { mutableFloatStateOf(0f) }
    var topBarHeightDp by remember { mutableStateOf(0.dp) }
    var isPostFling by remember { mutableStateOf(false) }
    var animatedOffset by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedTopBarOffset by animateFloatAsState(
        targetValue = animatedOffset,
        label = "",
        animationSpec = tween(durationMillis = if (isPostFling) 300 else 0)
    )
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                isPostFling = false

                val delta = available.y
                val newOffset = animatedOffset + delta
                animatedOffset = newOffset.coerceIn(-topBarHeightPx, 0f)

                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {

                isPostFling = true

                val top = 0f
                val middle = topBarHeightPx / 2
                val topMiddle = (top + middle) / 2
                val bottomMiddle = middle + topMiddle
                val bottom = topBarHeightPx
                val offset = abs(animatedOffset)

                animatedOffset = when {
                    top < offset && offset <= topMiddle -> { -top }
                    topMiddle < offset && offset <= middle -> { -middle }
                    middle < offset && offset <= bottomMiddle -> { -middle }
                    bottomMiddle < offset && offset <= bottom -> { -bottom }
                    offset > bottom -> { -bottom }
                    else -> { 0f }
                }

                return Velocity.Zero
            }
        }
    }

    /** Pull To Refresh */
    val refreshState = rememberPullToRefreshState()
    val refreshIconScale by animateFloatAsState(
        targetValue = if (!refreshState.isRefreshing) {
            refreshState.verticalOffset / 450f
        } else 1f,
        label = ""
    )
    LaunchedEffect(refreshState.isRefreshing) {
        if (refreshState.isRefreshing) {
            delay(2000L)
            refreshState.endRefresh()
        }
    }

    Box(modifier = modifier) {
        //TODO 리스트 맨 위에서 탑 바를 조금씩 올릴 시 공백이 생김, 처리 요망
        HomeTopBarWrapper(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged {
                    val padding = with(density) { CommonPaddingValues.TopBarVertical.toPx() }
                    topBarHeightPx = it.height + padding
                    val heightDp = with(density) { it.height.toDp() }
                    topBarHeightDp = heightDp + CommonPaddingValues.TopBarVertical
                },
            animatedTopBarOffset = animatedTopBarOffset
        ) {
            HomeTopBar(
                modifier = Modifier
                    .background(FlipTheme.colors.white)
                    .fillMaxWidth()
                    .padding(CommonPaddingValues.TopBarWithLogo),
                logo = R.drawable.ic_logo_dark,
                onSearchClick = { },
                onSettingClick = onSettingClick,
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

        /** Home Content */
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .padding(HomeScreenPaddingValues.Horizontal)
                .nestedScroll(refreshState.nestedScrollConnection)
        ) {
            if (postState.loading) {
                HomeSkeletonScreen(Modifier.padding(top = topBarHeightDp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnection),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp, alignment = Alignment.Top),
                    contentPadding = PaddingValues(bottom = 8.dp, top = topBarHeightDp),
                    state = lazyListState,
                ) {
                    //TODO 드문 확률이지만 ID가 겹치면 앱이 팅김
                    items(
                        items = postState.posts,
                        key = { post -> post.postId }
                    ) { post ->
                        HomeFlipCard(
                            modifier = Modifier.fillMaxWidth(),
                            post = post,
                            flipCardUiEvent = { flipCardUiEvent(it) },
                            reportAndBlockUiEvent = { uiEvent -> reportAndBlockUiEvent(uiEvent) }
                        )
                    }
                }
            }

            CustomPullToRefreshContainer(
                pullToRefreshState = refreshState,
                topPadding = (topBarHeightDp - 20.dp - CommonPaddingValues.TopBarVertical).coerceAtLeast(0.dp),
                animatedScale = refreshIconScale
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.CustomPullToRefreshContainer(
    pullToRefreshState: PullToRefreshState,
    topPadding: Dp,
    animatedScale: Float
) {
    PullToRefreshContainer(
        state = pullToRefreshState,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .zIndex(1f)
            .padding(top = topPadding)
            .graphicsLayer(
                scaleX = animatedScale,
                scaleY = animatedScale,
            ),
        containerColor = FlipTheme.colors.white,
        contentColor = FlipTheme.colors.main
    )
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
            flipCardUiEvent = { },
            reportAndBlockUiEvent = { },
            homeUiEvent = { },
            onSettingClick = { }
        )
    }
}