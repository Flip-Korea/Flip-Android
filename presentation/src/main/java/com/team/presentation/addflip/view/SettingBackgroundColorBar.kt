package com.team.presentation.addflip.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipTheme
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.asString
import com.team.presentation.R
import com.team.presentation.util.asColor

/**
 * 배경 컬러 설정 바
 *
 * @param selectedColor 선택된 컬러타입
 * @param isShowMoreClicked 색상 더보기 클릭 여부
 * @param showMore 색상 더보기 클릭 시
 * @param onSelectedColor 색상 선택 시
 */
@Composable
fun SettingBackgroundColorBar(
    modifier: Modifier = Modifier,
    selectedColor: BackgroundColorType,
    isShowMoreClicked: Boolean,
    showMore: () -> Unit,
    onSelectedColor: (BackgroundColorType) -> Unit,
) {

    val animateRotateValue =
        animateFloatAsState(targetValue = if (isShowMoreClicked) 90f else 0f, label = "")

    Row(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_background_color),
                contentDescription =
                stringResource(
                    id = R.string.add_flip_screen_content_desc_setting_background_color
                ),
                tint = FlipTheme.colors.main,
            )
            Text(
                text = stringResource(id = R.string.add_flip_screen_setting_background_color),
                style = FlipTheme.typography.body6,
                maxLines = 1,
            )
        }

        Row(
            modifier =
            Modifier.weight(2f).wrapContentWidth(Alignment.End).clickableSingleWithoutRipple {
                showMore()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End),
        ) animatedRow@{
            Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.CenterEnd) {
                this@animatedRow.AnimatedVisibility(
                    modifier = Modifier.wrapContentSize(),
                    visible = isShowMoreClicked,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                ) {
                    BackgroundColorOptions(
                        selectedColor = selectedColor,
                        onSelectedColor = { selectedColor ->
                            onSelectedColor(selectedColor)
                            showMore()
                        },
                    )
                }
                this@animatedRow.AnimatedVisibility(
                    modifier = Modifier.wrapContentSize(),
                    visible = !isShowMoreClicked,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                ) {
                    BackgroundColorOptionDisplay(selectedColor = selectedColor)
                }
            }

            Icon(
                modifier =
                Modifier.size(24.dp)
                    .graphicsLayer { rotationZ = animateRotateValue.value }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { showMore() },
                    ),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription =
                stringResource(id = R.string.add_flip_screen_content_desc_show_more),
                tint = FlipTheme.colors.gray5,
            )
        }
    }
}

/** 배경 컬러 설정 바(선택 옵션) */
@Composable
private fun BackgroundColorOptions(
    modifier: Modifier = Modifier,
    selectedColor: BackgroundColorType,
    onSelectedColor: (BackgroundColorType) -> Unit,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BackgroundColorType.entries.forEach { color ->
            val itemModifier =
                if (selectedColor == color) {
                    Modifier.clip(CircleShape)
                        .background(FlipTheme.colors.point3)
                        .border(1.dp, FlipTheme.colors.point, CircleShape)
                } else Modifier

            Box(modifier = Modifier.size(24.dp).then(itemModifier)) {
                Icon(
                    modifier =
                    Modifier.align(Alignment.Center)
                        .size(16.dp)
                        .border(1.dp, Color(0xFF212121), CircleShape)
                        .clickableSingleWithoutRipple { onSelectedColor(color) },
                    imageVector = Icons.Default.Circle,
                    contentDescription = "bg-color",
                    tint = color.asColor(),
                )
            }
        }
    }
}

/** 선택된 배경 컬러 표시 부분 */
@Composable
private fun BackgroundColorOptionDisplay(
    modifier: Modifier = Modifier,
    selectedColor: BackgroundColorType,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            modifier = Modifier.size(16.dp).border(1.dp, Color(0xFF212121), CircleShape),
            imageVector = Icons.Default.Circle,
            contentDescription = null,
            tint = selectedColor.asColor(),
        )
        Text(
            text = selectedColor.asString(),
            style = FlipTheme.typography.body5,
            color = FlipTheme.colors.gray5,
        )
    }
}