package com.team.presentation.editcategories.state

import com.team.presentation.util.uitext.UiText

data class MyCategoriesUpdateState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
