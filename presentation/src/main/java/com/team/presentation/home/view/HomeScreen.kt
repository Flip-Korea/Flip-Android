package com.team.presentation.home.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.skeleton.HomeSkeletonScreen
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.category.Category
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.presentation.R
import com.team.presentation.common.bottomsheet.ReportAndBlockUiEvent
import com.team.presentation.common.pullrefresh.FlipPullRefreshLazyColumn
import com.team.presentation.common.pullrefresh.PullRefreshConsumeState
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.HomeUiEvent
import com.team.presentation.home.state.PostState
import com.team.presentation.home.util.HomeScreenNestedScrollConnection
import com.team.presentation.home.util.HomeScreenPaddingValues
import com.team.presentation.util.CategoriesTestData
import com.team.presentation.util.fixedCategoriesSize
import kotlin.math.abs

/**
 * Flip의 메인 화면이자 홈 화면
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    myCategories: List<Category>,
    postState: PostState,
    onSettingClick: () -> Unit,
    flipCardUiEvent: (FlipCardUiEvent) -> Unit,
    reportAndBlockUiEvent: (ReportAndBlockUiEvent) -> Unit,
    homeUiEvent: (HomeUiEvent) -> Unit,
) {

    val density = LocalDensity.current

    val lazyListState = rememberLazyListState()

    var pullRefreshConsumeState by remember { mutableStateOf(PullRefreshConsumeState.Released) }

    /** TopBar Values */
    val topBarHeightDp  = with(density) { 310f.toDp() }
    val topBarHeightPx = with(density) { topBarHeightDp.toPx() }
    var topBarOffsetHeightPx by rememberSaveable { mutableFloatStateOf(0f) }
    var isPostFling by remember { mutableStateOf(false) }
    val animatedTopBarOffsetDp by animateDpAsState(
        targetValue = with(density) { topBarOffsetHeightPx.toDp() },
        label = "",
        animationSpec = tween(durationMillis = if (isPostFling) 300 else 0)
    )
    val nestedScrollConnection = remember {
        HomeScreenNestedScrollConnection(
            onPreScrollAction = { available ->
                if (pullRefreshConsumeState == PullRefreshConsumeState.Released) {
                    isPostFling = false

                    val delta = available.y
                    val newOffset = topBarOffsetHeightPx + delta
                    topBarOffsetHeightPx = newOffset.coerceIn(-topBarHeightPx, 0f)
                }
            },
            onPostFlingAction = {
                if (lazyListState.firstVisibleItemIndex != 0) {
                    isPostFling = true

                    val top = 0f
                    val middle = topBarHeightPx / 2
                    val topMiddle = (top + middle) / 2
                    val bottomMiddle = middle + topMiddle
                    val bottom = topBarHeightPx
                    val offset = abs(topBarOffsetHeightPx)

                    topBarOffsetHeightPx = when {
                        top < offset && offset <= topMiddle -> { -top }
                        topMiddle < offset && offset <= middle -> { -middle }
                        middle < offset && offset <= bottomMiddle -> { -middle }
                        bottomMiddle < offset && offset <= bottom -> { -bottom }
                        offset > bottom -> { -bottom }
                        else -> { 0f }
                    }
                }
            }
        )
    }

    /** Home Content */
    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {

        /** TopBar */
        HomeTopBarWrapper(
            animatedTopBarOffset = animatedTopBarOffsetDp,
            modifier = Modifier.fillMaxWidth(),
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
                items = myCategories,
                itemSplitSize = fixedCategoriesSize,
                onItemClick = { }
            )
        }

        /** 플립 카드뷰 리스트 */
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .padding(HomeScreenPaddingValues.Horizontal)
        ) {
            if (postState.loading) {
                HomeSkeletonScreen(Modifier.padding(top = topBarHeightDp))
            } else {
                FlipPullRefreshLazyColumn(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp, alignment = Alignment.Top),
                    contentPadding = PaddingValues(bottom = 8.dp, top = topBarHeightDp),
                    state = lazyListState,
                    onConsumeState = { pullRefreshConsumeState = it },
                    onRefresh = { },
                    userScrollEnabled = pullRefreshConsumeState != PullRefreshConsumeState.Refreshing
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
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun BoxScope.CustomPullToRefreshContainer(
//    pullToRefreshState: PullToRefreshState,
//    topPadding: Dp,
//    animatedAlpha: Float
//) {
//    PullToRefreshContainer(
//        state = pullToRefreshState,
//        modifier = Modifier
//            .align(Alignment.TopCenter)
//            .zIndex(1f)
//            .padding(top = topPadding)
//            .graphicsLayer(
//                alpha = animatedAlpha
//            ),
//        containerColor = FlipTheme.colors.white,
//        contentColor = FlipTheme.colors.main
//    )
//}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    FlipAppTheme {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            myCategories = CategoriesTestData.subList(0, 3),
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