package com.team.domain.model

data class Profile(
    val profileId: String,
    val nickname: String,
    val photoUrl: String,
    val postTotal: Int,
    val followerTotal: Int,
    val followingTotal: Int,
    val introduce: String
)
