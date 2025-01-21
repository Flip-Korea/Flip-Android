package com.team.presentation.register.state

import com.team.domain.type.SocialLoginPlatform
import com.team.presentation.common.image.FlipImage
import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage
import com.team.presentation.util.uitext.UiText

class RegisterContract {
    data class UiState(
        val previousLoginState: PreviousLoginState = PreviousLoginState(),
        val inputAgreementsState: InputAgreementsState = InputAgreementsState(),
        val inputNicknameState: InputNicknameState = InputNicknameState(),
        val inputIdState: InputIdState = InputIdState(),
        val inputImageState: InputImageState = InputImageState(),
        val registerFinishLoading: Boolean = false,
    ) : BaseUiState

    sealed class UiEvent : BaseUiEvent {
        data object CheckAll : UiEvent()

        data object UnCheckAll : UiEvent()

        data class OnToggleAgreementItem(
            val agreementItem: AgreementItem,
        ) : UiEvent()

        data class RequestToNextPage(
            val currentPage: RegisterScreenPage?,
        ) : UiEvent()

        data class OnNicknameChanged(
            val nickname: String,
        ) : UiEvent()

        data class OnIdChanged(
            val id: String,
        ) : UiEvent()

        data class OnImageChanged(
            val image: FlipImage,
        ) : UiEvent()

        data class SetPreviousLogin(
            val socialLoginPlatform: SocialLoginPlatform?,
            val oauthId: String?,
        ) : UiEvent()
    }

    sealed class UiEffect : BaseUiEffect {
        data object BackPress : UiEffect()

        data class NavigateTo(
            val destination: RegisterScreenPage,
        ) : UiEffect()

        data object NavigateToMain : UiEffect()

        data class ShowToast(
            val message: UiText,
        ) : UiEffect()
    }
}
