package com.team.presentation.register.viewmodel

import androidx.lifecycle.viewModelScope
import com.team.data.di.DefaultDispatcher
import com.team.domain.model.account.Login
import com.team.domain.model.account.NicknameValidationFactory
import com.team.domain.model.account.ProfileIdValidationFactory
import com.team.domain.model.account.Register
import com.team.domain.model.account.RegisterProfile
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.usecase.account.GetNicknameValidationResultUseCase
import com.team.domain.usecase.account.GetProfileIdValidationResultUseCase
import com.team.domain.usecase.register.RegisterUseCase
import com.team.domain.usecase.register.ValidateRegisterUseCases
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.validation.ValidationResult
import com.team.presentation.common.image.FlipImage
import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.register.state.InputAgreementsState
import com.team.presentation.register.state.InputIdValidState
import com.team.presentation.register.state.InputNicknameValidState
import com.team.presentation.register.state.RegisterContract
import com.team.presentation.register.state.isAdsAgree
import com.team.presentation.util.uitext.asUiText
import com.team.presentation.util.uitext.errorBodyFirst
import com.team.presentation.util.uitext.errorBodyReasonFirst
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
    private val registerUseCase: RegisterUseCase,
) : FlipBaseViewModel<
        RegisterContract.UiState,
        RegisterContract.UiEvent,
        RegisterContract.UiEffect,
    >() {
    override fun createInitialState(): RegisterContract.UiState =
        RegisterContract.UiState(
            inputAgreementsState =
                InputAgreementsState(
                    agreementCheckItems = AgreementItem.allCheckItems,
                ),
        )

    override suspend fun handleEvent(event: RegisterContract.UiEvent) {
        when (event) {
            RegisterContract.UiEvent.CheckAll -> checkAllAgreementItems(true)
            RegisterContract.UiEvent.UnCheckAll -> checkAllAgreementItems(false)
            is RegisterContract.UiEvent.OnToggleAgreementItem ->
                onToggleAgreementItem(event.agreementItem)

            is RegisterContract.UiEvent.RequestToNextPage -> requestToNextPage(event.currentPage)

            is RegisterContract.UiEvent.OnNicknameChanged -> onNameChanged(event.nickname)

            is RegisterContract.UiEvent.OnIdChanged -> onIdChanged(event.id)

            is RegisterContract.UiEvent.OnImageChanged -> onImageChanged(event.image)

            is RegisterContract.UiEvent.SetPreviousLogin ->
                setPreviousLoginState(event.socialLoginPlatform, event.oauthId)
        }
    }

    private fun setPreviousLoginState(
        socialLoginPlatform: SocialLoginPlatform?,
        oauthId: String?,
    ) {
        viewModelScope.launch {
            if (socialLoginPlatform == null || oauthId == null) {
                sendEffect {
                    RegisterContract.UiEffect.ShowToast(ErrorType.Auth.LOGIN_RETRY.asUiText())
                }
                return@launch
            }
            val login = Login(socialLoginPlatform, oauthId)
            val updatedPreviousLoginState = currentUiState.previousLoginState.copy(login = login)
            updateState {
                currentUiState.copy(previousLoginState = updatedPreviousLoginState)
            }
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
                finishRegister()
            }

            null -> {
                sendEffect {
                    RegisterContract.UiEffect.ShowToast(ErrorType.Exception.EXCEPTION.asUiText())
                }
            }
        }
    }

    private fun finishRegister() {
        viewModelScope.launch {
            val register = getRegisterData()
            if (register == null) {
                sendEffect {
                    RegisterContract.UiEffect.ShowToast(ErrorType.Auth.LOGIN_RETRY.asUiText())
                }
                return@launch
            }

            registerUseCase(register)
                .onEach { result ->
                    when (result) {
                        Result.Loading -> {
                            updateState {
                                currentUiState.copy(registerFinishLoading = true)
                            }
                        }

                        is Result.Error -> {
                            updateState {
                                currentUiState.copy(registerFinishLoading = false)
                            }
                            sendEffect {
                                RegisterContract.UiEffect.ShowToast(result.errorBodyFirst())
                            }
                        }

                        is Result.Success -> {
                            updateState {
                                currentUiState.copy(registerFinishLoading = true)
                            }
                            sendEffect {
                                RegisterContract.UiEffect.NavigateToMain
                            }
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun getRegisterData(): Register? =
        if (currentUiState.previousLoginState.login == null) {
            null
        } else {
            Register(
                socialLoginPlatform = currentUiState.previousLoginState.login!!.provider,
                oauthId = currentUiState.previousLoginState.login!!.oauthId,
                adsAgree = currentUiState.inputAgreementsState.isAdsAgree(),
                profile =
                    RegisterProfile(
                        userId = currentUiState.inputIdState.id,
                        nickname = currentUiState.inputNicknameState.name,
                        photoUrl = "", // 업로드 된 이미지 주소
                    ),
            )
        }

    private fun validateImage(image: FlipImage?) {
        if (image == null) {
            sendEffect {
                RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.Finish)
            }
            return
        }
        sendEffect {
            RegisterContract.UiEffect.NavigateTo(RegisterScreenPage.Finish)
        }
        return
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
                                        result.errorBodyReasonFirst(),
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
                                    InputNicknameValidState.Invalid(result.errorBodyReasonFirst()),
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
        val updatedAgreementCheckItems =
            currentUiState.inputAgreementsState.agreementCheckItems
                .map { it.key }
                .associateWith { value }
        val updatedInputAgreementsState =
            currentUiState.inputAgreementsState.copy(
                agreementCheckItems = updatedAgreementCheckItems,
            )
        updateState {
            currentUiState.copy(inputAgreementsState = updatedInputAgreementsState)
        }
    }

    private fun onToggleAgreementItem(item: AgreementItem) {
        val updatedAgreementCheckItems =
            currentUiState.inputAgreementsState.agreementCheckItems.toMutableMap()
        updatedAgreementCheckItems[item] = !updatedAgreementCheckItems[item]!!
        val updatedInputAgreementsState =
            currentUiState.inputAgreementsState.copy(
                agreementCheckItems = updatedAgreementCheckItems,
            )
        updateState {
            currentUiState.copy(inputAgreementsState = updatedInputAgreementsState)
        }
    }
}
