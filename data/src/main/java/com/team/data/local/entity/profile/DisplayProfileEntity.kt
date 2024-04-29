package com.team.data.local.entity.profile

import com.team.domain.model.profile.DisplayProfile

data class DisplayProfileEntity(
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
    val isFollower: Boolean,
    val isFollowing: Boolean,
    val introduce: String?,
    val followerCnt: Long?,
    val rating: String?,
)

fun DisplayProfileEntity.toExternal(): DisplayProfile =
    DisplayProfile(profileId, nickname, photoUrl, isFollower, isFollowing, introduce, followerCnt, rating)

fun DisplayProfile.toEntity(): DisplayProfileEntity =
    DisplayProfileEntity(profileId, nickname, photoUrl, isFollower, isFollowing, introduce, followerCnt, rating)