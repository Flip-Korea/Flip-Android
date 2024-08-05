package com.team.presentation.addflip.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun AddFlipContentTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    placeholder: String,
    content: String,
    onContentChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {

    BasicTextField(
        modifier = modifier
            .heightIn(min = 154.dp, max = (154 * 2).dp)
            .focusRequester(focusRequester)
            .focusable()
            .clickable {}
            .onFocusChanged {
                when {
                    it.isFocused -> onFocusChanged(it.isFocused)
                    it.isCaptured -> onFocusChanged(it.isCaptured)
                }
            },
        value = content,
        onValueChange = { onContentChanged(it) },
        textStyle = FlipTheme.typography.headline1,
        cursorBrush = SolidColor(FlipTheme.colors.point),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    ) { innerTextField ->

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.align(Alignment.TopStart)) {
                innerTextField()
                if (content.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = FlipTheme.typography.body5,
                        color = FlipTheme.colors.gray5
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddFlipContentTextFieldPreview() {

    val (title, onTitleChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { focusManager.clearFocus() }
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            AddFlipContentTextField(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                content = title,
                placeholder = "내용을 자유롭게 작성해보세요.",
                focusRequester = remember { FocusRequester() },
                onContentChanged = { onTitleChanged(it) },
                onFocusChanged = { }
            )
        }
    }
}