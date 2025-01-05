package com.team.presentation.register.viewmodel

import androidx.lifecycle.viewModelScope
import com.team.domain.usecase.account.GetNicknameValidationResultUseCase
import com.team.domain.usecase.account.GetProfileIdValidationResultUseCase
import com.team.domain.usecase.register.ValidateRegisterUseCases
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.common.snackbar.SnackbarAction
import com.team.presentation.common.snackbar.SnackbarController
import com.team.presentation.common.snackbar.SnackbarEvent
import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.InputIdValidState
import com.team.presentation.register.state.InputNicknameValidState
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.util.uitext.UiText
import com.team.presentation.util.uitext.asUiText
import com.team.presentation.util.uitext.errorBodyFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateRegisterUseCases: ValidateRegisterUseCases,
    private val getNicknameValidationResultUseCase: GetNicknameValidationResultUseCase,
    private val getProfileIdValidationResultUseCase: GetProfileIdValidationResultUseCase,
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

            is RegisterContract.UiEvent.OnNicknameChanged -> onNameChanged(event.nickname)

            is RegisterContract.UiEvent.OnIdChanged -> onIdChanged(event.id)
        }
    }

    private fun onIdChanged(id: String) {
        val inputIdState = currentUiState.inputIdState.copy(id = id)
        val updatedInputIdState =
            when (val validationResult = validateRegisterUseCases.validateInputIdUseCase(id)) {
                is ValidationResult.Error -> {
                    inputIdState.copy(
                        inputIdValidState =
                            InputIdValidState.Invalid(validationResult.error.asUiText()),
                    )
                }

                ValidationResult.Success -> {
                    inputIdState.copy(inputIdValidState = InputIdValidState.Valid)
                }
            }
        updateState { currentUiState.copy(inputIdState = updatedInputIdState) }
    }

    private fun onNameChanged(name: String) {
        val inputNameState = currentUiState.inputNicknameState.copy(name = name)
        val updatedInputNameState =
            when (
                val validationResult =
                    validateRegisterUseCases.validateInputNicknameUseCase(
                        name,
                    )
            ) {
                is ValidationResult.Error -> {
                    inputNameState.copy(
                        inputNicknameValidState =
                            InputNicknameValidState.Invalid(validationResult.error.asUiText()),
                    )
                }

                ValidationResult.Success -> {
                    inputNameState.copy(inputNicknameValidState = InputNicknameValidState.Valid)
                }
            }
        updateState { currentUiState.copy(inputNicknameState = updatedInputNameState) }
    }

    private fun requestToNextPage(currentPage: RegisterScreenPage?) {
        when (currentPage) {
            RegisterScreenPage.TermsOfService -> {
                sendEffect {
                    RegisterContract.UiEffect.NavigateTo(
                        RegisterScreenPage.InputNickname,
                    )
                }
            }

            RegisterScreenPage.InputNickname -> {
                validateNickname(currentUiState.inputNicknameState.name)
            }

            RegisterScreenPage.InputID -> {
                validateId(currentUiState.inputIdState.id)
            }

            RegisterScreenPage.InputPhoto -> TODO()
            null -> {
                viewModelScope.launch {
                    showSnackbar(message = ErrorType.Exception.EXCEPTION.asUiText())
                }
            }
        }
    }

    private fun validateId(profileId: String) {
        val inputIdState = currentUiState.inputIdState
        getProfileIdValidationResultUseCase(profileId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val updatedInputIdState =
                            inputIdState.copy(
                                inputIdValidState =
                                    InputIdValidState.Invalid(
                                        result.errorBodyFirst(),
                                    ),
                            )
                        updateState {
                            currentUiState.copy(
                                loading = false,
                                inputIdState = updatedInputIdState,
                            )
                        }
                    }
                    Result.Loading -> {
                        updateState {
                            currentUiState.copy(loading = true)
                        }
                    }
                    is Result.Success -> {
                        updateState { currentUiState.copy(loading = false) }
                        sendEffect {
                            RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.InputPhoto)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun validateNickname(nickname: String) {
        val inputNameState = currentUiState.inputNicknameState
        getNicknameValidationResultUseCase(nickname)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val updatedInputNameState =
                            inputNameState.copy(
                                inputNicknameValidState =
                                    InputNicknameValidState.Invalid(result.errorBodyFirst()),
                            )
                        updateState {
                            currentUiState.copy(
                                loading = false,
                                inputNicknameState = updatedInputNameState,
                            )
                        }
                    }

                    Result.Loading -> {
                        updateState { currentUiState.copy(loading = true) }
                    }

                    is Result.Success -> {
                        updateState { currentUiState.copy(loading = false) }
                        sendEffect {
                            RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.InputID)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun checkAllAgreementItems(value: Boolean) {
        val agreementItemChecks = currentUiState.agreementItemChecks.toMutableList()
        val mappedAgreementItemChecks = agreementItemChecks.map { value }
        updateState {
            currentUiState
                .copy(agreementItemChecks = mappedAgreementItemChecks.toList())
        }
    }

    private fun onToggleAgreementItem(itemIndex: Int) {
        val agreementItemChecks = currentUiState.agreementItemChecks.toMutableList()
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
