package com.team.presentation.common.bottomsheet.report

import com.team.presentation.util.UiText

data class ReportState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
