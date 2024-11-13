package com.team.presentation.addflip.state

import com.team.presentation.util.uitext.UiText

/**
 * 플립(Post)을 작성 및 저장할 때 사용되는 상태 모델
 */
data class AddPostState(
    val postSave: Boolean = false,
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
