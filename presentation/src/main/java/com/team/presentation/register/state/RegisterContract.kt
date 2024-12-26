package com.team.presentation.register.state

import com.team.presentation.common.util.BaseUiEffect
import com.team.presentation.common.util.BaseUiEvent
import com.team.presentation.common.util.BaseUiState
import com.team.presentation.register.AgreementItem
import com.team.presentation.util.uitext.UiText

class RegisterContract {
    data class UiState(
        val error: UiText = UiText.DynamicString(""),
        val loading: Boolean = false,
        val agreementItems: List<AgreementItem> = emptyList(),
    ) : BaseUiState

    sealed class UiEvent : BaseUiEvent {
        data object CheckAll : UiEvent()

        data object UnCheckAll : UiEvent()

        data object AgreementAndRegister : UiEvent()
    }

    sealed class UiEffect : BaseUiEffect
}
