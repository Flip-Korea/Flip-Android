package com.team.presentation.register.viewmodel

import com.team.domain.usecase.register.ValidateRegisterUseCases
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.util.uitext.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateRegisterUseCases: ValidateRegisterUseCases,
) : FlipBaseViewModel<RegisterContract.UiState, RegisterContract.UiEvent, RegisterContract.UiEffect>() {
    override fun createInitialState(): RegisterContract.UiState =
        RegisterContract.UiState.Success(
            agreementItems = AgreementItem.allItems,
            agreementItemChecks = List(AgreementItem.allItems.size) { false },
        )

    override suspend fun handleEvent(event: RegisterContract.UiEvent) {
        when (event) {
            RegisterContract.UiEvent.CheckAll -> checkAllAgreementItems(true)
            RegisterContract.UiEvent.UnCheckAll -> checkAllAgreementItems(false)
            is RegisterContract.UiEvent.OnToggleAgreementItem ->
                onToggleAgreementItem(
                    event.agreementItemIndex,
                )

            is RegisterContract.UiEvent.RequestToNextPage ->
                requestToNextPage(
                    event.currentRegisterScreenPage,
                )

            is RegisterContract.UiEvent.OnNameChanged -> onNameChanged(event.name)
        }
    }

    private fun onNameChanged(name: String) {
        val updatedInputNameState =
            when (val validationResult = validateRegisterUseCases.validateInputNameUseCase(name)) {
                is ValidationResult.Error -> {
                    (currentUiState as RegisterContract.UiState.Success)
                        .inputNameState
                        .copy(error = validationResult.error.asUiText())
                }

                ValidationResult.Success -> {
                    (currentUiState as RegisterContract.UiState.Success).inputNameState.copy(
                        name = name,
                    )
                }
            }
        updateState {
            (currentUiState as RegisterContract.UiState.Success)
                .copy(inputNameState = updatedInputNameState)
        }
    }

    private fun requestToNextPage(registerScreenPage: RegisterScreenPage) {
        when (registerScreenPage) {
            RegisterScreenPage.TERMS_OF_SERVICE -> {
                sendEffect { RegisterContract.UiEffect.GoToNextPage }
            }

            RegisterScreenPage.INPUT_NAME -> {
                validateInputName()
            }

            RegisterScreenPage.INPUT_ID -> TODO()
            RegisterScreenPage.INPUT_PHOTO -> TODO()
        }
    }

    private fun validateInputName() {
    }

    private fun checkAllAgreementItems(value: Boolean) {
        val agreementItemChecks =
            (currentUiState as RegisterContract.UiState.Success)
                .agreementItemChecks
                .toMutableList()
        val mappedAgreementItemChecks = agreementItemChecks.map { value }
        updateState {
            (currentUiState as RegisterContract.UiState.Success)
                .copy(agreementItemChecks = mappedAgreementItemChecks.toList())
        }
    }

    private fun onToggleAgreementItem(itemIndex: Int) {
        val agreementItemChecks =
            (currentUiState as RegisterContract.UiState.Success)
                .agreementItemChecks
                .toMutableList()
        agreementItemChecks[itemIndex] = !agreementItemChecks[itemIndex]
        updateState {
            (currentUiState as RegisterContract.UiState.Success)
                .copy(agreementItemChecks = agreementItemChecks.toList())
        }
    }
}
