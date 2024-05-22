package com.team.designsystem.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
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
import com.team.designsystem.component.utils.focusCleaner
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipSearchTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    focusManager: FocusManager,
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
            .height(40.dp)
            .focusCleaner(focusManager),
        value = text,
        onValueChange = onTextChanged,
        textStyle = FlipTheme.typography.body6,
        singleLine = true,
        interactionSource = interactionSource,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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
                modifier = Modifier.weight(1f).wrapContentSize(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = stringResource(id = R.string.content_desc_search),
                    tint = FlipTheme.colors.gray5
                )
                if (!focused && text.isEmpty()) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.tf_placeholder_search),
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
                    imageVector = ImageVector.vectorResource(R.drawable.ic_tf_remove),
                    contentDescription = stringResource(id = R.string.content_desc_tf_remove)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FlipSearchTextFieldPreview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipSearchTextField(
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
        )
    }
}