package com.team.presentation.common.bottomsheet.block

import com.team.presentation.util.uitext.UiText

data class BlockState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
