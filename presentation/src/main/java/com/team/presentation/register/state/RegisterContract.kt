package com.team.presentation.register.state

import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.RegisterScreenPage

class RegisterContract {
    data class UiState(
        val loading: Boolean = false,
        val agreementItems: List<AgreementItem> = emptyList(),
        val agreementItemChecks: List<Boolean> = emptyList(),
        val inputNicknameState: InputNicknameState = InputNicknameState(),
        val inputIdState: InputIdState = InputIdState(),
//            val registerDataInfo:
//            val register: ComposeRegister = ComposeRegister(),
    ) : BaseUiState

    sealed class UiEvent : BaseUiEvent {
        data object CheckAll : UiEvent()

        data object UnCheckAll : UiEvent()

        data class OnToggleAgreementItem(
            val agreementItemIndex: Int,
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
    }

    sealed class UiEffect : BaseUiEffect {
        data object BackPress : UiEffect()

        data class NavigateTo(
            val destination: RegisterScreenPage,
        ) : UiEffect()
    }
}
