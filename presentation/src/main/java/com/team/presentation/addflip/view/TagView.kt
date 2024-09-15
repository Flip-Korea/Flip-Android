package com.team.presentation.addflip.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.chip.FlipOutlinedSmallChip
import com.team.designsystem.component.textfield.FlipTextFieldStyles
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.common.bottomsheet.FlipModalBottomSheet


/**
 * 태그 선택 바텀시트 (메인)
 *
 * ### 사용 예시
 *     /** 태그 선택 바텀 시트 */
 *     if (showTagBottomSheet) {
 *         AddTagsBottomSheet(
 *             sheetState = tagSheetState,
 *             tags = newPostState.tags,
 *             onApply = { onUiEvent(AddFlipUiEvent.OnTagsChanged(tags = it)) },
 *             onDismissRequest = {
 *                 dismissRequester(
 *                     coroutineScope = coroutineScope,
 *                     sheetState = tagSheetState,
 *                     onDismissRequest = { keyboardController?.hide() },
 *                     onDismissCompletion = { showTagBottomSheet = false }
 *                 )
 *             }
 *         )
 *     }
 */
@Deprecated("초기 버전에서 제외")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTagsBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    tags: List<String>,
    onApply: (List<String>) -> Unit,
    onDismissRequest: () -> Unit,
) {

    var tempTags by remember(tags) { mutableStateOf(tags) }

    FlipModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) { bottomSheetModifier ->
        AddTagsBottomSheetContent(
            modifier = bottomSheetModifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            tags = tempTags,
            onAddTag = { tag ->
                if (tag.isNotEmpty() && tempTags.size < 10) {
                    tempTags = tempTags + tag
                }
            },
            onDeleteTag = { tag ->
                tempTags = tempTags - tag
            },
            onApply = {
                onApply(tempTags)
                onDismissRequest()
            }
        )
    }
}

/**
 * 태그 선택 바텀시트 (내부)
 */
@Deprecated("초기 버전에서 제외")
@Composable
private fun AddTagsBottomSheetContent(
    modifier: Modifier = Modifier,
    tags: List<String>,
    onAddTag: (String) -> Unit,
    onDeleteTag: (String) -> Unit,
    onApply: () -> Unit
) {

    val (text, onTextChanged) = remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp, alignment = Alignment.Top)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_tag),
                        contentDescription = stringResource(id = R.string.add_flip_screen_content_desc_add_tags),
                        tint = FlipTheme.colors.main
                    )
                    Text(
                        text = stringResource(id = R.string.add_flip_screen_add_tags_bottom_sheet_title),
                        style = FlipTheme.typography.headline3
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                        .clickableSingleWithoutRipple { onApply() },
                    text = stringResource(id = R.string.add_flip_screen_add_tags_save_btn),
                    style = FlipTheme.typography.headline3,
                    color = FlipTheme.colors.point
                )
            }

            TagSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                onTextChanged = { onTextChanged(it) },
                onAddTag = { tag ->
                    onAddTag(tag)
                    onTextChanged("")
                }
            )

            TagFlowRow(tags = tags, onDeleteTag = { tag -> onDeleteTag(tag) })

            FlipMediumButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp),
                enabled = text.isNotEmpty(),
                text = stringResource(id = R.string.add_flip_screen_add_tags_bottom_sheet_btn),
                onClick = {
                    onAddTag(text)
                    onTextChanged("")
                }
            )
        }
    }
}

/** 태그 들을 FlowRow 로 표시 */
@Deprecated("초기 버전에서 제외")
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagFlowRow(
    modifier: Modifier = Modifier,
    tags: List<String>,
    onDeleteTag: (String) -> Unit
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tags.forEach { tag ->
            FlipOutlinedSmallChip(
                text = tag,
                onClick = { onDeleteTag(tag) }
            )
        }
    }
}

/**
 * 태그 선택 바텀시트 (내부, 검색 바)
 */
@Composable
private fun TagSearchTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onAddTag: (String) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState().value

    val backgroundColor = if (!focused) {
        FlipTheme.colors.gray1
    } else FlipTheme.colors.white

    val borderColor = if (!focused) {
        Color.Transparent
    } else FlipTheme.colors.gray4

    BasicTextField(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerTextField)
            .border(1.dp, borderColor, FlipTheme.shapes.roundedCornerTextField)
            .fillMaxWidth()
            .height(40.dp),
        value = text,
        onValueChange = onTextChanged,
        textStyle = FlipTheme.typography.body6,
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onAddTag(text) }
        ),
        cursorBrush = FlipTextFieldStyles.cursorBrushBlack
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!focused && text.isEmpty()) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.add_flip_screen_add_tags_placeholder),
                        style = FlipTheme.typography.body5,
                        color = FlipTheme.colors.gray5,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }

            if (text.isNotEmpty()) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onTextChanged("") }
                        ),
                    imageVector = ImageVector.vectorResource(com.team.designsystem.R.drawable.ic_tf_remove),
                    contentDescription = stringResource(id = com.team.designsystem.R.string.content_desc_tf_remove)
                )
            }
        }
    }
}

/**
 * 태그 선택 바텀시트 (내부, 등록하기 버튼)
 */
@Deprecated("초기 버전에서 제외")
@Composable
private fun AddTagButton(showTagBottomSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(100.dp))
            .background(FlipTheme.colors.gray1)
            .border(1.dp, FlipTheme.colors.gray3, RoundedCornerShape(100.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = showTagBottomSheet
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.5.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(10.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                contentDescription = null,
                tint = FlipTheme.colors.gray5
            )
            Text(
                text = stringResource(id = R.string.add_flip_screen_add_tags_btn),
                style = FlipTheme.typography.body3,
                color = FlipTheme.colors.gray5
            )
        }
    }
}