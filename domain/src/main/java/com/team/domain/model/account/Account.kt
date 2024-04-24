package com.team.domain.model.account

import com.team.domain.model.profile.MyProfile

data class Account(
    val email: String?,
    val name: String?,
    val phoneNum: String?,
    val profiles: List<MyProfile>
)