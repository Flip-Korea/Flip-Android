package com.team.presentation.editcategories.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlinx.coroutines.channels.Channel

/**
 * 드래그 가능한 LazyList 에서 사용 한다.
 *
 * @param items 아이템 리스트
 * @param dragAndDropState DragAndDropState
 * @param keyProvider 아이템의 키를 지정 한다. (반드시 지정 해야 애니메이션이 적용 된다.)
 * @param content 아이템 Composable
 */
@OptIn(ExperimentalFoundationApi::class)
inline fun <T : Any> LazyListScope.draggableItems(
    items: List<T>,
    dragAndDropState: DragAndDropState,
    crossinline keyProvider: (Int, T) -> Any,
    crossinline content: @Composable (Modifier, Int, T) -> Unit,
) {

    itemsIndexed(
        items = items,
        //TODO 해당 파라미터를 적용하면 왼쪽으로 스크롤 시 이슈 발생 해서 일단 제거,
        // 제거 하면 이슈는 사라지지만 애니메이션 적용 안됨
//        key = { index, item -> keyProvider(index, item) },
        contentType = { index, _ -> DraggableItem(index = index) }
    ) { index, item ->

        val modifier = if (dragAndDropState.draggingItemIndex == index) {
            Modifier
                .zIndex(1f)
                .graphicsLayer {
                    when (dragAndDropState.dragDirection) {
                        DragDirection.Horizontal -> {
                            translationX = dragAndDropState.delta
                        }
                        DragDirection.Vertical -> {
                            translationY = dragAndDropState.delta
                        }
                    }
                }
        } else {
            Modifier
                .animateItemPlacement()
        }

        content(modifier, index, item)
    }
}

fun Modifier.dragContainer(dragDropState: DragAndDropState): Modifier {
    return this.then(pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset = offset)
            },
            onDragStart = { offset -> dragDropState.onDragStart(offset) },
            onDragEnd = { dragDropState.onDragInterrupted() },
            onDragCancel = { dragDropState.onDragInterrupted() }
        )
    }
    )
}

@Composable
fun rememberDragAndDropState(
    dragDirection: DragDirection,
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit,
    draggableItemsNum: Int
): DragAndDropState {

    val state =
        remember(lazyListState) {
            DragAndDropState(
                dragDirection = dragDirection,
                draggableItemsNum = draggableItemsNum,
                lazyListState = lazyListState,
                onMove = onMove,
            )
        }
    LaunchedEffect(state) {
        while (true) {
            val diff = state.scrollChannel.receive()
            lazyListState.scrollBy(diff)
        }
    }
    return state
}

class DragAndDropState(
    val dragDirection: DragDirection,
    private val draggableItemsNum: Int,
    private val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit
) {

    var draggingItemIndex: Int? by mutableStateOf(null)

    var delta by mutableFloatStateOf(0f)

    val scrollChannel = Channel<Float>()

    private var draggingItem: LazyListItemInfo? = null

    internal fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo
            // offset 이 현재 선택한 Item 영역 내부에 있는지 확인 하고 null 이 아니면 반환
            .firstOrNull { item ->
                val targetOffset = when(dragDirection) {
                    DragDirection.Horizontal -> offset.x.toInt()
                    DragDirection.Vertical -> offset.y.toInt()
                }
                targetOffset in item.offset..(item.offset + item.size)
            }
            ?.also {
                // item 이랑 item index 저장
                (it.contentType as? DraggableItem)?.let { draggableItem ->
                    draggingItem = it
                    draggingItemIndex = draggableItem.index
                }
            }
    }

    internal fun onDragInterrupted() {
        draggingItem = null
        draggingItemIndex = null
        delta = 0f
    }

    internal fun onDrag(offset: Offset) {
        // offset 의 변화량
        delta += when (dragDirection) {
            DragDirection.Horizontal -> offset.x
            DragDirection.Vertical -> offset.y
        }

        val currentDraggingItemIndex =
            draggingItemIndex ?: return
        val currentDraggingItem =
            draggingItem ?: return

        val startOffset = currentDraggingItem.offset + delta
        val endOffset =
            currentDraggingItem.offset + currentDraggingItem.size + delta
        val middleOffset = startOffset + (endOffset - startOffset) / 2

        val targetItem =
            lazyListState.layoutInfo.visibleItemsInfo.find { item ->
                middleOffset.toInt() in item.offset..item.offset + item.size &&
                        currentDraggingItem.index != item.index &&
                        item.contentType is DraggableItem
            }

        if (targetItem != null) {
            val targetIndex = (targetItem.contentType as DraggableItem).index
            onMove(currentDraggingItemIndex, targetIndex)
            draggingItemIndex = targetIndex
            delta += currentDraggingItem.offset - targetItem.offset
            draggingItem = targetItem
        } else {
            val startOffsetToTop = startOffset - lazyListState.layoutInfo.viewportStartOffset
            val endOffsetToBottom = endOffset - lazyListState.layoutInfo.viewportEndOffset
//            val scrollMultiplier = 0.5f // 스크롤 속도를 조절하는 인자
            val scroll =
                when {
                    startOffsetToTop < 0 -> startOffsetToTop.coerceAtMost(0f)
                    endOffsetToBottom > 0 -> endOffsetToBottom.coerceAtLeast(0f)
                    else -> 0f
                }

            if (scroll != 0f && currentDraggingItemIndex != 0 && currentDraggingItemIndex != draggableItemsNum - 1) {
                scrollChannel.trySend(scroll)
            }
        }
    }
}

data class DraggableItem(val index: Int)