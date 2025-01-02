package com.team.presentation.register.viewmodel

import androidx.lifecycle.viewModelScope
import com.team.domain.usecase.register.ValidateRegisterUseCases
import com.team.domain.util.ErrorType
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.common.snackbar.SnackbarAction
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateRegisterUseCases: ValidateRegisterUseCases,
) : FlipBaseViewModel<
        RegisterContract.UiState,
        RegisterContract.UiEvent,
        RegisterContract.UiEffect,
    >() {
    override fun createInitialState(): RegisterContract.UiState =
        RegisterContract.UiState(
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

            is RegisterContract.UiEvent.RequestToNextPage -> requestToNextPage(event.currentPage)

            is RegisterContract.UiEvent.OnNameChanged -> onNameChanged(event.name)
        }
    }

    private fun onNameChanged(name: String) {
        val updatedInputNameState =
            when (val validationResult = validateRegisterUseCases.validateInputNameUseCase(name)) {
                is ValidationResult.Error -> {
                    currentUiState
                        .inputNameState
                        .copy(error = validationResult.error.asUiText())
                }

                ValidationResult.Success -> {
                    currentUiState.inputNameState.copy(name = name)
                }
            }
        updateState { currentUiState.copy(inputNameState = updatedInputNameState) }
    }

    private fun requestToNextPage(currentPage: RegisterScreenPage?) {
        when (currentPage) {
            RegisterScreenPage.TermsOfService -> {
                sendEffect { RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.InputName) }
            }

            RegisterScreenPage.InputName -> {
                validateInputName()
            }

            RegisterScreenPage.InputID -> TODO()
            RegisterScreenPage.InputPhoto -> TODO()
            null -> {
                viewModelScope.launch {
                    showSnackbar(message = ErrorType.Exception.EXCEPTION.asUiText())
                }
            }
        }
    }

    private fun validateInputName() {
    }

    private fun checkAllAgreementItems(value: Boolean) {
        val agreementItemChecks =
            currentUiState
                .agreementItemChecks
                .toMutableList()
        val mappedAgreementItemChecks = agreementItemChecks.map { value }
        updateState {
            currentUiState
                .copy(agreementItemChecks = mappedAgreementItemChecks.toList())
        }
    }

    private fun onToggleAgreementItem(itemIndex: Int) {
        val agreementItemChecks =
            currentUiState
                .agreementItemChecks
                .toMutableList()
        agreementItemChecks[itemIndex] = !agreementItemChecks[itemIndex]
        updateState {
            currentUiState.copy(agreementItemChecks = agreementItemChecks.toList())
        }
    }

    private suspend fun showSnackbar(
        message: UiText,
        action: SnackbarAction? = null,
    ) {
        SnackbarController.sendEvent(event = SnackbarEvent(message = message, action = action))
    }
}
