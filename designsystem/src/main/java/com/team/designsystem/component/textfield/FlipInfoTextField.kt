package com.team.designsystem.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.designsystem.R
import com.team.designsystem.component.utils.focusCleaner
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipInfoTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    focusManager: FocusManager,
    maxLength: Int,
    valid: Boolean,
    errorMessage: String? = null,
    placeholder: String? = null,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState().value

    val placeholderEnabled = placeholder != null && !focused && text.isEmpty()

    val backgroundColor = if (placeholderEnabled) {
        FlipTheme.colors.gray1
    } else {
        FlipTheme.colors.white
    }

    val borderColor = if (placeholderEnabled) Color.Transparent else {
        when {
            errorMessage != null -> FlipTheme.colors.statusRed
            valid -> FlipTheme.colors.point
            else -> FlipTheme.colors.gray4
        }
    }
    val counterColor = when {
        errorMessage != null -> FlipTheme.colors.statusRed
        valid -> FlipTheme.colors.point
        else -> FlipTheme.colors.gray6
    }

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            modifier = Modifier
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
            cursorBrush = FlipTextFieldStyles.cursorBrushPoint,
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.CenterStart)
                ) {
                    if (placeholderEnabled) {
                        Text(
                            modifier = Modifier,
                            text = placeholder ?: "",
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

        if (!placeholderEnabled) {
            BottomSection(
                text = text,
                errorMessage = errorMessage,
                maxLength = maxLength,
                counterColor = counterColor
            )
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    text: String,
    errorMessage: String? = null,
    maxLength: Int,
    counterColor: Color,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        if (errorMessage != null) {
            Row(
                modifier = Modifier
                    .weight(3f)
                    .wrapContentSize(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(16.dp, 14.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_warning),
                    contentDescription = stringResource(id = R.string.content_desc_error)
                )
                Text(
                    text = errorMessage,
                    style = FlipTheme.typography.body3,
                    color = FlipTheme.colors.statusRed
                )
            }
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.CenterEnd),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = counterColor)) {
                    append(text.length.toString())
                }
                withStyle(style = SpanStyle(letterSpacing = 2.sp)) {
                    append("/")
                }
                append(maxLength.toString())
            },
            style = FlipTheme.typography.body3,
            color = FlipTheme.colors.gray6
        )
    }
}

@Preview(name = "placeholder", showBackground = true)
@Composable
private fun FlipInfoTextFieldPreview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            valid = false,
            placeholder = "placeholder"
        )
    }
}

@Preview(name = "default", showBackground = true)
@Composable
private fun FlipInfoTextField1Preview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            valid = false,
            placeholder = null
        )
    }
}

@Preview(name = "error", showBackground = true)
@Composable
private fun FlipInfoTextField2Preview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            valid = false,
            errorMessage = "Error Helper Text",
            placeholder = null
        )
    }
}

@Preview(name = "valid", showBackground = true)
@Composable
private fun FlipInfoTextField3Preview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            valid = true,
            placeholder = null
        )
    }
}

@Preview(name = "valid & error both", showBackground = true)
@Composable
private fun FlipInfoTextField4Preview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    FlipAppTheme {
        FlipInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            valid = true,
            errorMessage = "Error Helper Text",
            placeholder = null
        )
    }
}

@Preview(name = "dynamic", showBackground = true)
@Composable
private fun FlipInfoTextField5Preview() {

    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var valid by remember { mutableStateOf(false) }
    var errorMessage: String? by remember { mutableStateOf(null) }

    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlipInfoTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = text,
                onTextChanged = onTextChanged,
                focusManager = focusManager,
                maxLength = 10,
                valid = valid,
                errorMessage = errorMessage,
                placeholder = "5자 이상 10자 이하로 작성."
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val result = fakeValidation(text)
                    valid = result.first
                    errorMessage = result.second
                }
            ) {
                Text(text = "validation")
            }

            TextField(value = text, onValueChange = onTextChanged)
        }
    }
}

private fun fakeValidation(text: String): Pair<Boolean, String?> {
    return when (text.length) {
        in 5..10 -> Pair(true, null)
        else -> Pair(false, "5자 이상 10자 이하로 작성해주세요.")
    }
}