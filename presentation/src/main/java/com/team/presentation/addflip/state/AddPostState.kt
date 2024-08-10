package com.team.presentation.addflip.state

import com.team.presentation.util.UiText

data class AddPostState(
    val tempPostSave: Boolean = false,
    val postSave: Boolean = false,
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
