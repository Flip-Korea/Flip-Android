package com.team.presentation.register.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
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
import com.team.domain.usecase.register.ValidateInputIdUseCase
import com.team.presentation.R
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.InputIdState
import com.team.presentation.register.state.InputIdValidState
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.util.uitext.UiText

@Composable
fun InputIdScreen(
    modifier: Modifier = Modifier,
    currentStep: Int,
    totalSteps: Int,
    inputIdState: InputIdState,
    onUiEvent: (RegisterContract.UiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
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
                    append(stringResource(id = R.string.register_screen_input_id_title_1))
                    append("\n")
                    withStyle(FlipTheme.typography.headline8.toSpanStyle()) {
                        append(
                            stringResource(
                                id = R.string.register_screen_input_id_title_2,
                            ),
                        )
                    }
                    append(stringResource(id = R.string.register_screen_input_id_title_3))
                },
            )
            Spacer(modifier = Modifier.height(64.dp))
            FlipInfoTextField(
                infoTextFieldState = inputIdState.inputIdValidState.toInfoTextFieldState(),
                text = inputIdState.id,
                onTextChanged = {
                    onUiEvent(RegisterContract.UiEvent.OnIdChanged(it))
                },
                focusManager = focusManager,
                maxLength = ValidateInputIdUseCase.MAX_LENGTH,
                placeholder =
                stringResource(
                    id = R.string.register_screen_input_id_placeholder,
                ),
                leadingIcon = {
                    Text(text = EmailPrefix, style = FlipTheme.typography.body6)
                },
            )
        }

        RegisterScreenBottomBar(
            title = stringResource(id = R.string.register_screen_input_id_btn),
            enabled = true,
            isLoading = inputIdState.loading,
            onClick = {
                onUiEvent(
                    RegisterContract.UiEvent.RequestToNextPage(RegisterScreenPage.InputID)
                )
            },
        )
    }
}

@Composable
private fun InputIdValidState.toInfoTextFieldState(): InfoTextFieldState =
    when (this) {
        InputIdValidState.Idle -> InfoTextFieldState.Idle
        is InputIdValidState.Invalid -> {
            val defaultError =
                UiText
                    .StringResource(R.string.register_screen_common_input_retry)
                    .asString()
            InfoTextFieldState.Error(this.error?.asString() ?: defaultError)
        }

        InputIdValidState.Valid -> InfoTextFieldState.Valid
    }

private const val EmailPrefix = "@ "

@Preview(showBackground = true)
@Composable
private fun InputIdScreenPreview() {
    FlipAppTheme {
        InputIdScreen(
            currentStep = 2,
            totalSteps = 3,
            inputIdState = InputIdState(),
            onUiEvent = { },
        )
    }
}
