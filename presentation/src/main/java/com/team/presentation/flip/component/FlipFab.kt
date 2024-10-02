package com.team.presentation.flip.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.dropShadow
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import kotlinx.coroutines.delay

/**
 *  Flip Floating Action Button
 *
 *  #### Usage With Wrapper Composable
 *          Box(
 *             modifier = Modifier
 *                 .fillMaxSize()
 *                 .pointerInput(Unit) {
 *                     detectTapGestures(onTap = {
 *                         isExpanded = false
 *                     })
 *                 }
 *         ) {
 *              // ...
 *              FlipFab()
 *              // ...
 *         }
 *
 *  @param isExpanded 확장 여부
 *  @param liked 좋아요 여부
 *  @param scraped 스크랩 여부
 *  @param fabEvent Fab 이벤트 [FabEvent]
 *  @param changeExpanded isExpanded 스위칭 작업
 *  @param onDismissRequest FabMenu 숨기는 작업
 */
@Composable
fun FlipFab(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    liked: Boolean,
    scraped: Boolean,
    fabEvent: (FabEvent) -> Unit,
    changeExpanded: () -> Unit,
    onDismissRequest: () -> Unit
) {

//    // Expanded Switch
//    var isExpanded by remember { mutableStateOf(false) }
    val animateRotation = remember { Animatable(0f) }
    LaunchedEffect(isExpanded) {
        animateRotation.animateTo(
            targetValue = if (isExpanded) 45f else 0f,
        )
    }

    // Prevent Double Click
    var enableAgain by remember { mutableStateOf(true) }
    LaunchedEffect(enableAgain) {
        if (enableAgain) return@LaunchedEffect
        delay(150)
        enableAgain = true
    }

    // Fab Items
    val fabItems = remember { mutableStateOf(fabItems) }

    if (isExpanded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .pointerInput(Unit) {
                    detectTapGestures {
                        onDismissRequest()
                    }
                }
        )
    }

    Column(
        modifier = modifier
            .wrapContentSize()
            .fillMaxSize()
            .zIndex(2f),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Bottom)
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(durationMillis = 150)),
            exit = fadeOut(tween(durationMillis = 150))
        ) {
            FabMenu(
                items = fabItems.value,
                liked = liked,
                scraped = scraped,
                fabEvent = fabEvent
            )
        }
        Box(
            modifier = Modifier
                .dropShadow(
                    color = Color(0xFF636363).copy(0.2f),
                    offsetY = 2.dp,
                    blurRadius = 8.dp,
                    borderRadius = 100.dp
                )
                .size(48.dp)
                .clip(CircleShape)
                .background(FlipTheme.colors.main)
                .clickableSingle {
                    if (enableAgain) {
                        enableAgain = false
                        changeExpanded()
                    }
                }
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(16.dp)
                    .rotate(animateRotation.value),
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                contentDescription = "더보기",
                tint = Color.White
            )
        }
    }
}

/**
 * Fab Menu
 *
 * @param items Fab 아이템
 * @param liked 좋아요 여부
 * @param scraped 스크랩 여부
 * @param fabEvent Fab 이벤트 [FabEvent]
 */
@Composable
private fun FabMenu(
    modifier: Modifier = Modifier,
    items: List<FabItem>,
    liked: Boolean,
    scraped: Boolean,
    fabEvent: (FabEvent) -> Unit,
) {

    Column(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerLarge)
            .background(FlipTheme.colors.main)
            .wrapContentSize()
            .padding(vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items.forEachIndexed { index, item ->

            var isActive by rememberSaveable {
                mutableStateOf(
                    when (item) {
                        FabItem.Like -> liked
                        FabItem.Scrap -> scraped
                        else -> false
                    }
                )
            }
            val icon = if (isActive) item.filledIcon else item.outlinedIcon

            MenuItem(
                index = index,
                color = item.color,
                title = item.title,
                icon = icon,
                onClick = {
                    /** 좋아요나 스크랩 버튼 클릭 시 발동 */
                    if (item.switchable) {
                        isActive = !isActive
                        fabEvent(item.fabEvent) // 실제로 수행할 작업 처리
                    }
                }
            )
        }
    }
}

/**
 * Fab Menu Item
 *
 * @param index 현재 메뉴 인덱스
 * @param color 아이템 색상
 * @param title 아이템 제목 리소스
 * @param icon 아이템 아이콘 리소스
 * @param onClick 아이템 클릭 시 수행할 작업
 */
@Composable
private fun MenuItem(
    modifier: Modifier = Modifier,
    index: Int,
    color: Color,
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .widthIn(max = 109.dp)
            .fillMaxWidth()
            .clickableSingle { onClick() },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .wrapContentSize()
                .padding(start = 12.dp, top = 5.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(
                12.dp,
                alignment = Alignment.Start
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = stringResource(id = title),
                tint = color
            )
            Text(
                text = stringResource(id = title),
                style = FlipTheme.typography.body5,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FlipFabPreview() {

    // Expanded Switch
    var isExpanded by remember { mutableStateOf(true) }

    FlipAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        isExpanded = false
                    })
                }
        ) {
            FlipFab(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                isExpanded = isExpanded,
                liked = false,
                scraped = false,
                fabEvent = { },
                changeExpanded = { isExpanded = !isExpanded },
                onDismissRequest = { isExpanded = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 80, heightDp = 200)
@Preview(showBackground = true, widthDp = 45, heightDp = 200)
@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
private fun FabMenuPreview() {
    FabMenu(
        items = fabItems,
        liked = false,
        scraped = false,
        fabEvent = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun FabMenuEventPreview() {

    var (text, onTextChanged) = remember {
        mutableStateOf("Waiting Event...")
    }
    // Expanded Switch
    var isExpanded by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(modifier = Modifier.align(Alignment.Center), text = text, fontSize = 40.sp)
        FlipFab(
            isExpanded = isExpanded,
            liked = true,
            scraped = false,
            changeExpanded = { isExpanded = !isExpanded },
            onDismissRequest = { isExpanded = false },
            fabEvent = {
                when (it) {
                    FabEvent.OnLikeClick -> { onTextChanged("OnLikeClick") }
                    FabEvent.OnScrapClick -> { onTextChanged("OnScrapClick") }
                    FabEvent.OnCommentClick -> { onTextChanged("OnCommentClick") }
                    FabEvent.OnMoreClick -> { onTextChanged("OnMoreClick") }
                }
            }
        )
    }
}