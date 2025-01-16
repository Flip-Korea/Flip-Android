package com.team.presentation.register.viewmodel

import androidx.lifecycle.viewModelScope
import com.team.data.di.DefaultDispatcher
import com.team.domain.model.account.NicknameValidationFactory
import com.team.domain.model.account.ProfileIdValidationFactory
import com.team.domain.usecase.account.GetNicknameValidationResultUseCase
import com.team.domain.usecase.account.GetProfileIdValidationResultUseCase
import com.team.domain.usecase.register.ValidateRegisterUseCases
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.common.image.FlipImage
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val validateRegisterUseCases: ValidateRegisterUseCases,
    private val getNicknameValidationResultUseCase: GetNicknameValidationResultUseCase,
    private val getProfileIdValidationResultUseCase: GetProfileIdValidationResultUseCase,
    private val nicknameValidationFactory: NicknameValidationFactory,
    private val profileIdValidationFactory: ProfileIdValidationFactory,
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
                onToggleAgreementItem(event.agreementItemIndex)

            is RegisterContract.UiEvent.RequestToNextPage -> requestToNextPage(event.currentPage)

            is RegisterContract.UiEvent.OnNicknameChanged -> onNameChanged(event.nickname)

            is RegisterContract.UiEvent.OnIdChanged -> onIdChanged(event.id)

            is RegisterContract.UiEvent.OnImageChanged -> onImageChanged(event.image)
        }
    }

    private fun onImageChanged(image: FlipImage) {
        viewModelScope.launch(defaultDispatcher) {
            val updatedInputImageState =
                currentUiState.inputImageState.copy(image = image)
            updateState {
                currentUiState.copy(inputImageState = updatedInputImageState)
            }
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

            RegisterScreenPage.InputImage -> {
                validateImage(currentUiState.inputImageState.image)
            }

            RegisterScreenPage.Finish -> {
                // TODO 회원가입 완료시키기
            }

            null -> {
                viewModelScope.launch {
                    showSnackbar(message = ErrorType.Exception.EXCEPTION.asUiText())
                }
            }
        }
    }

    private fun validateImage(image: FlipImage?) {
        if (image == null) {
            sendEffect {
                RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.Finish)
            }
            return
        }
        // TODO 이미지 업로드 후 주소 반환받기
    }

    private fun validateId(profileId: String) {
        val inputIdState = currentUiState.inputIdState
        val profileIdValidation = profileIdValidationFactory.create(profileId)
        getProfileIdValidationResultUseCase(profileIdValidation)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val updatedInputIdState =
                            inputIdState.copy(
                                inputIdValidState =
                                    InputIdValidState.Invalid(
                                        result.errorBodyFirst(),
                                    ),
                                loading = false,
                            )
                        updateState {
                            currentUiState.copy(inputIdState = updatedInputIdState)
                        }
                    }

                    Result.Loading -> {
                        val updatedInputIdState = inputIdState.copy(loading = true)
                        updateState {
                            currentUiState.copy(inputIdState = updatedInputIdState)
                        }
                    }

                    is Result.Success -> {
                        val updatedInputIdState = inputIdState.copy(loading = false)
                        updateState {
                            currentUiState.copy(inputIdState = updatedInputIdState)
                        }
                        sendEffect {
                            RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.InputImage)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun validateNickname(nickname: String) {
        val inputNameState = currentUiState.inputNicknameState
        val nicknameValidation = nicknameValidationFactory.create(nickname)
        getNicknameValidationResultUseCase(nicknameValidation)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val updatedInputNameState =
                            inputNameState.copy(
                                inputNicknameValidState =
                                    InputNicknameValidState.Invalid(result.errorBodyFirst()),
                                loading = false,
                            )
                        updateState {
                            currentUiState.copy(
                                inputNicknameState = updatedInputNameState,
                            )
                        }
                    }

                    Result.Loading -> {
                        val updatedInputNameState = inputNameState.copy(loading = true)
                        updateState {
                            currentUiState.copy(inputNicknameState = updatedInputNameState)
                        }
                    }

                    is Result.Success -> {
                        val updatedInputNameState = inputNameState.copy(loading = false)
                        updateState {
                            currentUiState.copy(inputNicknameState = updatedInputNameState)
                        }
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
