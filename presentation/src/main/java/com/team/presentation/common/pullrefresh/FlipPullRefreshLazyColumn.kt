package com.team.presentation.common.pullrefresh

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.designsystem.theme.FlipAppTheme
import com.team.presentation.util.pullrefresh.pullRefresh
import com.team.presentation.util.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Pull To Refresh 기능 사용 시 전달할 소비 상태
 * /TODO: 뷰모델로 상태 값 옮겨서 일회성이벤트로 처리하는 방식도 고려
 */
enum class PullRefreshConsumeState { Pulling, Released, Refreshing }

/**
 * Pull To Refresh(당겨서 새로고침)이 적용된 LazyColumn
 *
 * @param state LazyListState
 * @param contentPadding LazyColumn의 기본 인자 값
 * @param reverseLayout LazyColumn의 기본 인자 값
 * @param verticalArrangement LazyColumn의 기본 인자 값
 * @param horizontalAlignment LazyColumn의 기본 인자 값
 * @param flingBehavior LazyColumn의 기본 인자 값
 * @param userScrollEnabled LazyColumn의 기본 인자 값
 * @param onConsumeState [PullRefreshConsumeState] 소비 상태
 * @param onRefresh 새고 로침 시 수행할 작업
 * @param content LazyColumn 내부 컨텐츠
 */
@Composable
fun FlipPullRefreshLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    onConsumeState: (PullRefreshConsumeState) -> Unit,
    onRefresh: () -> Unit,
    content: LazyListScope.() -> Unit,
) {

    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidthDp.toPx() }
    val iconSizePx = with(density) { iconSizeDp.toPx() }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        refreshThreshold = positionalThresholdDp,
        onRefresh = {
            onRefresh()
            //TODO: 임시 코드, 반드시 삭제할 것
            coroutineScope.launch {
                isRefreshing = true
                // Mimic refresh
                delay(2000)
                isRefreshing = false
            }
        })

    // 드래그 하는 동안 오프셋 Y의 위치 애니메이션화
    val offsetYAnimation by animateIntAsState(targetValue = when {
        isRefreshing -> refreshingOffsetDelta
        pullRefreshState.progress in 0f..1f -> (offsetDelta * pullRefreshState.progress).roundToInt()
        pullRefreshState.progress > 1f -> (offsetDelta + ((pullRefreshState.progress - 1f) * .1f) * 100).roundToInt()
        else -> 0
    }, label = "OffsetYAnimation")

    // 드래그 하는 동안 알파 값 애니메이션화
    val alphaAnimation by animateFloatAsState(targetValue = when {
        isRefreshing -> 0.3f
        (1 - pullRefreshState.progress * 10) > 0.3f -> 1 - pullRefreshState.progress * 10
        (1 - pullRefreshState.progress * 10) < 0.3f -> 0.3f
        else -> 1f
    }, label = "AlphaAnimation")

    // 드래그 하는 동안 스케일 값 애니메이션
    val scaleAnimation by animateFloatAsState(targetValue = when {
        isRefreshing -> 1.2f
        pullRefreshState.progress + 1 > 1.2f -> 1.2f
        pullRefreshState.progress + 1 < 1.2f -> pullRefreshState.progress + 1
        else -> 1f
    }, label = "ScaleAnimation")

    // Pull 중인 상태
    val pullingState by remember {
        derivedStateOf {
            if (pullRefreshState.progress > 0f) {
                PullRefreshConsumeState.Pulling
            } else PullRefreshConsumeState.Released
        }
    }
    LaunchedEffect(pullingState) {
        onConsumeState(pullingState)
    }

    // Pull 완료 상태 (return true or false)
    val pullCompleted by remember {
        derivedStateOf {
            pullRefreshState.progress >= 1f
        }
    }

    // Pull 완료 상태에 대한 스케일 값 애니메이션화
    val scaleAnimationOnPullCompleted = remember {
        Animatable(initialValue = 1f)
    }
    // 햅틱 피드백
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(pullCompleted) {
        if (pullCompleted) {
            // Pull 완료 시 요소 바운싱 효과
            scaleAnimationOnPullCompleted.animateTo(1.17f)
            scaleAnimationOnPullCompleted.animateTo(1f)

            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    // 새로고침 중 움직일 경로 애니메이션화
    val pathBackOnRefreshing = remember {
        Animatable(initialValue = 0f)
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            onConsumeState(PullRefreshConsumeState.Refreshing)
            pathBackOnRefreshing.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse)
            )
        } else {
            onConsumeState(PullRefreshConsumeState.Released)
            pathBackOnRefreshing.snapTo(0f)
        }
    }

    // Pull Progress 값 애니메이션화
    val pullProgressAnimation by animateFloatAsState(
        targetValue = pullRefreshState.progress,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "PullProgressAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState)
    ) {
        Box(
            modifier = Modifier
                .offset(y = contentPadding.calculateTopPadding() - additionalPadding)
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            // 드래그 중 표시될 부분
            Canvas(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        translationY = offsetYAnimation.dp.toPx() - iconSizePx
                        scaleX = scaleAnimation
                        scaleY = scaleAnimation
                    }
                    .graphicsLayer {
                        scaleX = scaleAnimationOnPullCompleted.value
                        scaleY = scaleAnimationOnPullCompleted.value
                    },
                onDraw = {
                    val convertedValue = pullProgressAnimation * 360

                    if (!pullRefreshState.refreshing) {
                        drawArc(
                            brush = SolidColor(strokeColor),
                            startAngle = 270f,
                            sweepAngle = convertedValue,
                            useCenter = false,
                            style = Stroke(strokeWidthPx, cap = StrokeCap.Round),
                            size = Size(iconSizePx, iconSizePx),
                            topLeft = center.copy(x = center.x - iconSizePx / 2)
                        )
                    }
                }
            )

            // 드래그 완료 시 표시될 부분
            if (pullRefreshState.refreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(iconSizeDp)
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            translationY = offsetYAnimation.dp.toPx() + iconSizePx
                            scaleX = scaleAnimation
                            scaleY = scaleAnimation
                        }
                        .graphicsLayer {
                            scaleX = scaleAnimationOnPullCompleted.value
                            scaleY = scaleAnimationOnPullCompleted.value
                        },
                    color = strokeColor,
                    strokeWidth = strokeWidthDp,
                    strokeCap = StrokeCap.Round
                )
            }
        }

        LazyColumn(
            modifier = modifier
                .graphicsLayer {
                    translationY = offsetYAnimation.dp.toPx()
                },
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled
        ) {
            content()
        }
    }
}

private val strokeWidthDp = 2.dp
private val strokeColor = Color(0xFF3B82F6)
private val iconSizeDp = 30.dp
/**
 * 새로 고침 트리거 거리
 * 새로 고침을 하기 위해 얼마나 많이 화면을 아래로 당겨야 하는 지를 결정
 */
private val positionalThresholdDp = 30.dp
/** [positionalThresholdDp]와 유사하나,
 *  새로 고침을 위해 당겨야 하는 오프셋이 시각 적으로 표현 되는데 도움을 준다.
 */
private const val offsetDelta = 70
/**
 * 새로 고침 후 아이콘의 오프셋
 */
private const val refreshingOffsetDelta = 0
/** 리스트와 아이콘 사이 간격 추가 여백 */
private val additionalPadding = 20.dp

@Preview(showBackground = true)
@Composable
private fun FlipPullRefreshLazyColumn2Preview() {

    val items = remember {
        List(100) { it }
    }

    FlipAppTheme {
        FlipPullRefreshLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 50.dp),
            onConsumeState = { },
            onRefresh = { }
        ) {
            items(items) { item ->
                Text(text = "Item $item")
            }
        }
    }
}