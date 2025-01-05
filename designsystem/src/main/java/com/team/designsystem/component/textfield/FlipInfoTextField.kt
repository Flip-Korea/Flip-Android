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
import androidx.compose.runtime.saveable.rememberSaveable
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

/** 정보를 입력 받는 텍스트 필드 사용 시 명확한 상태 제어를 위해 사용 */
sealed interface InfoTextFieldState {
    /** 기본 상태 */
    data object Idle : InfoTextFieldState

    /** 유효한 상태 (정상적인 상황, 유효성 검사 후 유효성이 확인 되었을 때) */
    data object Valid : InfoTextFieldState

    /** 에러 상태 */
    data class Error(
        val errorMessage: String,
    ) : InfoTextFieldState
}

/**
 * 정보를 입력 받는 텍스트필드, 회원가입 단계에서 사용된다.
 *
 * @param text 입력 값
 * @param onTextChanged 입력 값 변경 시
 * @param focusManager [FocusManager]
 * @param maxLength 텍스트필드 제한 길이
 */
@Composable
fun FlipInfoTextField(
    modifier: Modifier = Modifier,
    infoTextFieldState: InfoTextFieldState,
    text: String,
    onTextChanged: (String) -> Unit,
    focusManager: FocusManager,
    maxLength: Int,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState().value

    val placeholderEnabled by rememberSaveable(focused, text) {
        mutableStateOf(!focused && text.isEmpty())
    }

    val backgroundColor =
        if (placeholderEnabled) {
            FlipTheme.colors.gray1
        } else {
            FlipTheme.colors.white
        }

    val borderColor =
        if (placeholderEnabled) {
            Color.Transparent
        } else {
            when (infoTextFieldState) {
                InfoTextFieldState.Idle -> FlipTheme.colors.gray4
                InfoTextFieldState.Valid -> FlipTheme.colors.point
                is InfoTextFieldState.Error -> FlipTheme.colors.statusRed
            }
        }
    val counterColor =
        when (infoTextFieldState) {
            InfoTextFieldState.Idle -> FlipTheme.colors.gray4
            InfoTextFieldState.Valid -> FlipTheme.colors.point
            is InfoTextFieldState.Error -> FlipTheme.colors.statusRed
        }

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            modifier =
                Modifier
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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!placeholderEnabled) {
                    leadingIcon?.let { leadingIcon() }
                }

                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.CenterStart),
                ) {
                    if (placeholderEnabled) {
                        Text(
                            modifier = Modifier,
                            text = placeholder ?: "",
                            style = FlipTheme.typography.body5,
                            color = FlipTheme.colors.gray5,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                }

                if (text.isNotEmpty()) {
                    Image(
                        modifier =
                            Modifier
                                .size(24.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { onTextChanged("") },
                                ),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_tf_remove),
                        contentDescription = stringResource(id = R.string.content_desc_tf_remove),
                    )
                }
            }
        }

        BottomSection(
            infoTextFieldState = infoTextFieldState,
            text = text,
            maxLength = maxLength,
            counterColor = counterColor,
        )
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    infoTextFieldState: InfoTextFieldState,
    text: String,
    maxLength: Int,
    counterColor: Color,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
    ) {
        if (infoTextFieldState is InfoTextFieldState.Error) {
            Row(
                modifier =
                    Modifier
                        .weight(3f)
                        .wrapContentSize(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(16.dp, 14.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_warning),
                    contentDescription = stringResource(id = R.string.content_desc_error),
                )
                Text(
                    text = infoTextFieldState.errorMessage,
                    style = FlipTheme.typography.body3,
                    color = FlipTheme.colors.statusRed,
                )
            }
        }
        Text(
            modifier =
                Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.CenterEnd),
            text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = counterColor)) {
                        append(text.length.toString())
                    }
                    withStyle(style = SpanStyle(letterSpacing = 2.sp)) {
                        append("/")
                    }
                    append(maxLength.toString())
                },
            style = FlipTheme.typography.body3,
            color = FlipTheme.colors.gray6,
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            infoTextFieldState = InfoTextFieldState.Idle,
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            placeholder = "placeholder",
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            infoTextFieldState = InfoTextFieldState.Idle,
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            placeholder = null,
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            infoTextFieldState = InfoTextFieldState.Error("Error Helper Text"),
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            placeholder = null,
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            infoTextFieldState = InfoTextFieldState.Valid,
            text = text,
            onTextChanged = onTextChanged,
            focusManager = focusManager,
            maxLength = 30,
            placeholder = null,
        )
    }
}

@Preview(name = "dynamic", showBackground = true)
@Composable
private fun FlipInfoTextField5Preview() {
    val (text, onTextChanged) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var infoTextFieldState: InfoTextFieldState by remember {
        mutableStateOf(InfoTextFieldState.Idle)
    }

    FlipAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlipInfoTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                infoTextFieldState = infoTextFieldState,
                text = text,
                onTextChanged = onTextChanged,
                focusManager = focusManager,
                maxLength = 10,
                placeholder = "5자 이상 10자 이하로 작성.",
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    infoTextFieldState = fakeValidation(text)
                },
            ) {
                Text(text = "validation")
            }

            TextField(value = text, onValueChange = onTextChanged)
        }
    }
}

private fun fakeValidation(text: String): InfoTextFieldState =
    when (text.length) {
        in 5..10 -> InfoTextFieldState.Valid
        else -> InfoTextFieldState.Error("5자 이상 10자 이하로 작성해주세요.")
    }
