package com.team.presentation.util.uitext

import com.team.domain.util.SuccessType
import com.team.presentation.R

fun SuccessType.asUiText(): UiText =
    when (this) {
        SuccessType.TempPost.SAVE -> UiText.StringResource(R.string.success_temppost_save)
        SuccessType.TempPost.DELETE -> UiText.StringResource(R.string.success_temppost_delete)
    }