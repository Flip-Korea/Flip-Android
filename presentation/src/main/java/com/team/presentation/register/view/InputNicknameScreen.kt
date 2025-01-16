package com.team.presentation.register.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.textfield.FlipInfoTextField
import com.team.designsystem.component.textfield.InfoTextFieldState
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.usecase.register.ValidateInputNicknameUseCase
import com.team.presentation.R
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.InputNicknameState
import com.team.presentation.register.state.InputNicknameValidState
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.util.uitext.UiText

@Composable
fun InputNicknameScreen(
    modifier: Modifier = Modifier,
    currentStep: Int,
    totalSteps: Int,
    inputNicknameState: InputNicknameState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RegisterProgressView(
                modifier = Modifier.fillMaxWidth(),
                currentStep = currentStep,
                totalSteps = totalSteps,
            )
            RegisterTitleView(
                mainTitle =
                    buildAnnotatedString {
                        append(
                            stringResource(
                                id = R.string.register_screen_input_nickname_title_1,
                            ),
                        )
                        append("\n")
                        withStyle(FlipTheme.typography.headline8.toSpanStyle()) {
                            append(
                                stringResource(
                                    id = R.string.register_screen_input_nickname_title_2,
                                ),
                            )
                        }
                        append(
                            stringResource(
                                id = R.string.register_screen_input_nickname_title_3,
                            ),
                        )
                    },
            )
            Spacer(modifier = Modifier.height(64.dp))
            FlipInfoTextField(
                infoTextFieldState =
                    inputNicknameState.inputNicknameValidState
                        .toInfoTextFieldState(),
                text = inputNicknameState.name,
                onTextChanged = {
                    onUiEvent(RegisterContract.UiEvent.OnNicknameChanged(it))
                },
                focusManager = focusManager,
                maxLength = ValidateInputNicknameUseCase.MAX_LENGTH,
                placeholder =
                    stringResource(
                        id = R.string.register_screen_input_nickname_placeholder,
                    ),
            )
        }

        RegisterScreenBottomBar(
            title = stringResource(id = R.string.register_screen_input_nickname_btn),
            enabled = true,
            isLoading = inputNicknameState.loading,
            onClick = {
                onUiEvent(
                    RegisterContract.UiEvent.RequestToNextPage(RegisterScreenPage.InputNickname),
                )
            },
        )
    }
}

@Composable
private fun InputNicknameValidState.toInfoTextFieldState(): InfoTextFieldState =
    when (this) {
        InputNicknameValidState.Idle -> InfoTextFieldState.Idle
        is InputNicknameValidState.Invalid -> {
            val defaultError =
                UiText
                    .StringResource(R.string.register_screen_common_input_retry)
                    .asString()
            InfoTextFieldState.Error(this.error?.asString() ?: defaultError)
        }

        InputNicknameValidState.Valid -> InfoTextFieldState.Valid
    }

@Preview(showBackground = true)
@Composable
private fun InputNicknameScreenPreview() {
    FlipAppTheme {
        InputNicknameScreen(
            modifier = Modifier.fillMaxSize(),
            currentStep = 1,
            totalSteps = 3,
            inputNicknameState = InputNicknameState(),
            onUiEvent = { },
        )
    }
}
