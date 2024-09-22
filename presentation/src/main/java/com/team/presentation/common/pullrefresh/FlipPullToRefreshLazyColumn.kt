package com.team.presentation.common.pullrefresh

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.presentation.util.pullrefresh.PullToRefreshBox
import com.team.presentation.util.pullrefresh.PullToRefreshStateM3
import com.team.presentation.util.pullrefresh.rememberPullToRefreshStateM3
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Pull To Refresh 기능 사용 시 전달할 소비 상태 */
enum class PullToRefreshConsumeState { Pulling, Released, Refreshing }

/**
 * Flip 에서 사용되는 PullToRefresh(당겨서 새로고침)용 LazyColumn
 *
 * @param pullToRefreshState [PullToRefreshStateM3]
 * @param additionalPadding 추가 여백 (Ex. 탑바 영역)
 * @param isRefreshing 새로고침 여부
 * @param onRefresh 새로고침 시 수행할 작업
 * @param onConsumeState [PullToRefreshConsumeState] PullToRefresh 소비 상태 제어
 * @param content (수직 으로) 스크롤 가능한 리스트 (Ex. LazyColumn, Scrollable Column)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipPullToRefreshWrapper(
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshStateM3,
    additionalPadding: Dp,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onConsumeState: (PullToRefreshConsumeState) -> Unit = { },
    content: @Composable (Modifier) -> Unit,
) {

    // PullToRefreshConsumeState 제어
    val pulling = remember {
        derivedStateOf { pullToRefreshState.distanceFraction > 0f }
    }
    LaunchedEffect(pullToRefreshState.distanceFraction, isRefreshing) {
        when {
            isRefreshing -> onConsumeState(PullToRefreshConsumeState.Refreshing)
            pulling.value -> onConsumeState(PullToRefreshConsumeState.Pulling)
            else -> onConsumeState(PullToRefreshConsumeState.Released)
        }
    }

    // 컨텐츠 Offset
    val contentAnimatedOffset by animateDpAsState(
        targetValue = when {
            isRefreshing -> RefreshSectionMaxHeight.dp
            pullToRefreshState.distanceFraction in 0f..1f -> (RefreshSectionMaxHeight * pullToRefreshState.distanceFraction).dp
            pullToRefreshState.distanceFraction > 1f -> {
//                (RefreshSectionMaxHeight + ((pullToRefreshState.distanceFraction - 1f) * .1f) * RefreshSectionMaxHeight).dp
                (RefreshSectionMaxHeight * pullToRefreshState.distanceFraction).dp
            }

            else -> 0.dp
        }, label = "Content Animated Offset"
    )

    // Pull 완료 상태 (return true or false)
    val pullCompleted by remember {
        derivedStateOf { pullToRefreshState.distanceFraction >= 1f }
    }
    // Pull 완료 상태에 대한 스케일 값 애니메이션화
    val scaleAnimationOnPullCompleted = remember {
        Animatable(initialValue = 1f)
    }

    // 기기에서 '터치 피드백'이 활성화 되어있어야 함
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(pullCompleted) {
        if (pullCompleted) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            scaleAnimationOnPullCompleted.animateTo(1.17f)
            scaleAnimationOnPullCompleted.animateTo(1f)
        }
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        indicator = { }
    ) {
        // 로딩 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                .height(RefreshSectionMaxHeight.dp)
                .padding(vertical = VerticalPadding.dp)
                .padding(top = additionalPadding)
                .graphicsLayer {
                    scaleX = scaleAnimationOnPullCompleted.value
                    scaleY = scaleAnimationOnPullCompleted.value
                }
                .zIndex(0f),
            contentAlignment = Alignment.Center
        ) {
            FlipPullRefreshIndicator(
                progress = pullToRefreshState.distanceFraction,
                isLoading = isRefreshing,
                size = IndicatorSize.dp
            )
        }

        content(
            Modifier
                .graphicsLayer {
                    translationY = contentAnimatedOffset
                        .roundToPx()
                        .toFloat()
                }
                .zIndex(1f)
        )
    }
}

/** 로딩 인디케이터 사이즈 */
private const val IndicatorSize = 50

/** 로딩 인디케이터 기준으로 위, 아래 패딩 */
private const val VerticalPadding = 14

/**
 * '당겨서 새로고침' 영역의 최대 높이(오프셋)
 *
 * ([IndicatorSize] + [VerticalPadding] * 2)
 */
private const val RefreshSectionMaxHeight = IndicatorSize + VerticalPadding * 2

/**
 * '당겨서 새로고침' 아이콘의 최대 높이(오프셋)
 *
 * ([RefreshSectionMaxHeight] / 2) - (아이콘 사이즈 / 2)
 */
private const val RefreshIconMaxHeight = (RefreshSectionMaxHeight / 2) - (IndicatorSize / 2)


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PullRefreshScreenPreview() {

    val list = List(30) { "#$it" }
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshStateM3()

    FlipPullToRefreshWrapper(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        additionalPadding = 0.dp,
        pullToRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(2_000L)
                isRefreshing = false
            }
        },
    ) { contentModifier ->
        LazyColumn(
            modifier = contentModifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(list) { index, item ->
                Box {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray
                        ),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 7.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = item,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}