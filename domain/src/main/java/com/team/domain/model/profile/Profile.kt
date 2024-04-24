package com.team.domain.model.profile

/** 다른 사용자의 프로필 **/
data class Profile(
    val profileId: String,
    val nickname: String,
    val introduce: String,
    val photoUrl: String,
    val postCnt: Int,
    val followerCnt: Int,
    val followingCnt: Int,
    val isFollower: Boolean,
    val isFollowing: Boolean,
    val rating: String
)
