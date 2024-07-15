package com.team.presentation.editcategories.state

import com.team.domain.model.profile.MyProfile
import com.team.presentation.util.UiText

data class MyProfileState(
    val myProfile: MyProfile? = null,
    val loading: Boolean = false,
    val error: UiText = UiText.DynamicString("")
)
