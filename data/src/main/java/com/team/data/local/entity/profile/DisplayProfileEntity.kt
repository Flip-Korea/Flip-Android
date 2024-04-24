package com.team.data.local.entity.profile

import com.team.domain.model.profile.DisplayProfile

data class DisplayProfileEntity(
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
    val following: Boolean,
    val followerCnt: Long?
)

fun DisplayProfileEntity.toExternal(): DisplayProfile =
    DisplayProfile(profileId, nickname, photoUrl, following, followerCnt)

fun DisplayProfile.toEntity(): DisplayProfileEntity =
    DisplayProfileEntity(profileId, nickname, photoUrl, following, followerCnt)