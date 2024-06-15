package com.team.presentation.post.component

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
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.dropShadow
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import kotlinx.coroutines.delay

/**
 *  @param changeExpanded isExpanded 스위칭 해주는 고차함수
 *
 */
@Composable
fun FlipFab(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    changeExpanded: () -> Unit,
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

    val fabItems = remember { mutableStateOf(fabItems) }

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(durationMillis = 150)),
            exit = fadeOut(tween(durationMillis = 150))
        ) {
            FabMenu(items = fabItems.value)
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

@Composable
private fun FabMenu(
    modifier: Modifier = Modifier,
    items: List<FabItem>,
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
            MenuItem(index = index, item = item)
        }
    }
}

@Composable
private fun MenuItem(
    modifier: Modifier = Modifier,
    index: Int,
    item: FabItem,
) {

    // 끌어올려야 함
    var isActive by remember { mutableStateOf(item.active) }
    val icon = if (isActive) item.filledIcon else item.outlinedIcon

    Box(
        modifier = modifier
            .widthIn(max = 109.dp)
            .fillMaxWidth()
            .clickableSingle {
                if (index == 0 || index == 1) {
                    isActive = !isActive
                }
            },
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
                contentDescription = stringResource(id = item.title),
                tint = item.color
            )
            Text(
                text = stringResource(id = item.title),
                style = FlipTheme.typography.body5,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 200, heightDp = 350)
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
                changeExpanded = { isExpanded = !isExpanded }
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
    FabMenu(items = fabItems)
}