package com.team.presentation.addflip.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.textfield.FlipTextFieldStyles
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun AddFlipTitleTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    title: String,
    onTitleChanged: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        modifier = modifier,
        value = title,
        onValueChange = { onTitleChanged(it) },
        textStyle = FlipTheme.typography.headline1,
        cursorBrush = FlipTextFieldStyles.cursorBrushPoint,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
    ) { innerTextField ->

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 3.dp, bottom = 3.dp)
            ) {
                innerTextField()
                if (title.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = FlipTheme.typography.body5,
                        color = FlipTheme.colors.gray5
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = FlipTheme.colors.gray3
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddFlipTitleTextFieldPreview() {

    val (title, onTitleChanged) = remember { mutableStateOf("") }

    FlipAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            AddFlipTitleTextField(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                title = title,
                placeholder = "제목",
                onTitleChanged = { onTitleChanged(it) }
            )
        }
    }
}