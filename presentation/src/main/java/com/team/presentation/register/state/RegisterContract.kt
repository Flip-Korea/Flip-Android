package com.team.presentation.register.state

import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.model.ComposeRegister

class RegisterContract {
    sealed class UiState : BaseUiState {
        data class Error(
            val message: String,
        ) : UiState()

        data object Loading : UiState()

        data class Success(
            val agreementItems: List<AgreementItem> = emptyList(),
            val agreementItemChecks: List<Boolean> = emptyList(),
            val register: ComposeRegister = ComposeRegister(),
        ) : UiState()
    }

    sealed class UiEvent : BaseUiEvent {
        data object CheckAll : UiEvent()

        data object UnCheckAll : UiEvent()

        data class OnToggleAgreementItem(
            val agreementItemIndex: Int,
        ) : UiEvent()
    }

    sealed class UiEffect : BaseUiEffect
}
