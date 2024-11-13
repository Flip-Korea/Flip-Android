package com.team.presentation.common.bottomsheet

sealed interface ReportAndBlockUiEvent {

    data class OnReport(
        val profileId: String,
        val photoUrl: String
    ): ReportAndBlockUiEvent

    data class OnBlock(
        val profileId: String,
        val photoUrl: String
    ): ReportAndBlockUiEvent
}