package com.team.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.focusCleaner
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
private fun CommentButton(
    modifier: Modifier = Modifier,
    color: Color,
    onComment: () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickableSingle { onComment() },
        shape = CircleShape,
        color = color
    ) {
        Icon(
            modifier = Modifier
                .size(12.dp, 15.dp)
                .padding(horizontal = 9.dp, vertical = 8.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_up),
            contentDescription = stringResource(id = R.string.content_desc_error),
            tint = Color.White
        )
    }
}

@Composable
fun FlipCommentTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    focusManager: FocusManager,
    onComment: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState().value

    BasicTextField(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerTextField)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 47.dp)
            .wrapContentHeight()
            .focusCleaner(focusManager),
        value = text,
        onValueChange = onTextChanged,
        textStyle = FlipTheme.typography.body5,
        interactionSource = interactionSource,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        cursorBrush = FlipTextFieldStyles.cursorBrushPoint
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(FlipTheme.colors.gray1)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .wrapContentSize(Alignment.CenterStart),
            ) {
                if (!focused && text.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.tf_placeholder_comment),
                        style = FlipTheme.typography.body5,
                        color = FlipTheme.colors.gray5,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else { innerTextField() }
            }

            CommentButton(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(31.dp),
                color = if (text.isNotEmpty()) {
                    FlipTheme.colors.point
                } else FlipTheme.colors.gray3,
                onComment = { if (text.isNotEmpty()) onComment() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipCommentTextFieldPreview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipCommentTextField(
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            onComment = { }
        )
    }
}

@Preview
@Composable
private fun CommentButtonPreview() {
    FlipAppTheme {
        CommentButton(
            modifier = Modifier.size(31.dp),
            color = FlipTheme.colors.point,
            onComment = { }
        )
    }
}