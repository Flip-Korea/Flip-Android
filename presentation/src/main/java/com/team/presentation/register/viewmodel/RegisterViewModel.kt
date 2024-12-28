package com.team.presentation.register.viewmodel

import com.team.presentation.common.util.FlipBaseViewModel
import com.team.presentation.register.AgreementItem
import com.team.presentation.register.state.RegisterContract

class RegisterViewModel :
    FlipBaseViewModel<
        RegisterContract.UiState,
        RegisterContract.UiEvent,
        RegisterContract.UiEffect,
    >() {
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
        }
    }

    private fun checkAllAgreementItems(value: Boolean) {
        val agreementItemChecks =
            (currentUiState as RegisterContract.UiState.Success).agreementItemChecks.toMutableList()
        val mappedAgreementItemChecks = agreementItemChecks.map { value }
        updateState {
            (currentUiState as RegisterContract.UiState.Success)
                .copy(agreementItemChecks = mappedAgreementItemChecks.toList())
        }
    }

    private fun onToggleAgreementItem(itemIndex: Int) {
        val agreementItemChecks =
            (currentUiState as RegisterContract.UiState.Success).agreementItemChecks.toMutableList()
        agreementItemChecks[itemIndex] = !agreementItemChecks[itemIndex]
        updateState {
            (currentUiState as RegisterContract.UiState.Success)
                .copy(agreementItemChecks = agreementItemChecks.toList())
        }
    }
}
