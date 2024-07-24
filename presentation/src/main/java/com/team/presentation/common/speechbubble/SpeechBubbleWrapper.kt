package com.team.presentation.common.speechbubble

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

/**
 * SpeechBubble(말풍선)을 사용할 수 있는 Wrapper
 *
 * @param showed 말풍선 활성화 여부
 * @param tipStartOffset 팁의 시작 오프셋 (IntOffset 이고 적용 하고 싶은 요소의 위치/높이 값이 필요)
 * @param onDismissRequest Popup 종료 시
 * @param speechBubbleView 말풍선 Composable (해당 Wrapper 를 사용 하는 곳에서 인자의 Modifier 를 연결 해서 사용)
 * @param content 말풍선을 사용할 Composable
 */
@Composable
fun SpeechBubbleWrapper(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    showed: Boolean,
    tipStartOffset: IntOffset = IntOffset(10, 0),
    onDismissRequest: () -> Unit,
    speechBubbleView: @Composable (Modifier) -> Unit,
    content: @Composable () -> Unit,
) {

    val density = LocalDensity.current
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }
    val pivotFractionX = with(density) { tipStartOffset.x.toDp().toPx() / width }

    var showedForPopup by remember { mutableStateOf(false) }
    LaunchedEffect(showed) {
        if (showed) {
            delay(ENABLED_DELAY)
            showedForPopup = true
        }
    }

    if (!enabled) {
        content()
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        onDismissRequest()
                    }
                }
        ) {
            Box(
                modifier = modifier
                    .onSizeChanged {
                        width = it.width
                        height = it.height
                    }
            ) {
                content()

                if (showedForPopup) {
                    CustomPopup(
                        showed = showed,
                        onDismissRequest = { },
                        popupPositionProvider = object : PopupPositionProvider {
                            override fun calculatePosition(
                                anchorBounds: IntRect,
                                windowSize: IntSize,
                                layoutDirection: LayoutDirection,
                                popupContentSize: IntSize,
                            ): IntOffset {
                                /** 내부 에서 직접 요소의 경계 선을 찾고 싶을 때 사용 */
//                            return IntOffset(
//                                x = anchorBounds.left,
//                                y = anchorBounds.bottom
//                            )
                                return IntOffset(
                                    x = tipStartOffset.x,
                                    y = tipStartOffset.y
                                )
                            }
                        },
                        //TODO 애니메이션 디자인시스템에 넣기
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 300, easing = EaseOutCubic)
                        ) + scaleIn(
                            initialScale = 0.3f,
                            transformOrigin = TransformOrigin(pivotFractionX, 0f),
                            animationSpec = tween(durationMillis = 300, easing = EaseOutBack)
                        ),
                        exit = fadeOut(
                            animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic)
                        ) + scaleOut(
                            targetScale = 0.3f,
                            transformOrigin = TransformOrigin(pivotFractionX, 0f),
                            animationSpec = tween(durationMillis = 300, easing = EaseInOutBack)
                        )
                    ) {
                        speechBubbleView(Modifier.zIndex(1f))
                    }
                }
            }
        }
    }

}

@Composable
private fun CustomPopup(
    modifier: Modifier = Modifier,
    showed: Boolean,
    popupPositionProvider: PopupPositionProvider,
    onDismissRequest: (() -> Unit)? = null,
    focusable: Boolean = false,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = showed

    if(expandedState.currentState || expandedState.targetState || !expandedState.isIdle) {
        Popup(
            popupPositionProvider = popupPositionProvider,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = focusable)
        ) {
            AnimatedVisibility(
                visibleState = expandedState,
                enter = enter,
                exit = exit,
                modifier = modifier,
                content = content
            )
        }
    }
}

private val ENABLED_DELAY = 500L

@Preview(showBackground = true)
@Composable
private fun SpeechBubbleWrapperPreview() {

    var speechBubbleEnabled by remember { mutableStateOf(false) }
    val tipStartOffset by remember { mutableStateOf(IntOffset(10, 0)) }

    SpeechBubbleWrapper(
        modifier = Modifier,
        enabled = true,
        showed = speechBubbleEnabled,
        tipStartOffset = tipStartOffset,
        onDismissRequest = { },
        speechBubbleView = {
            SpeechBubbleView(
                modifier = it,
                containerColor = Color.LightGray.copy(0.8f),
                text = "말풍선 테스트! 말풍선 테스트! 말풍선 테스트!",
                tipStartOffset = tipStartOffset.x.dp
            )
        }
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .background(Color.Green)
                    .clickable { speechBubbleEnabled = !speechBubbleEnabled }
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .background(Color.LightGray)
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .background(Color.Green)
            )
        }
    }
}