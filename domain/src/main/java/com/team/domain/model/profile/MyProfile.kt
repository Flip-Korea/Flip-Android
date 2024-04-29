package com.team.domain.model.profile

/** 내 프로필 **/
data class MyProfile(
    // 플립 ID
    val profileId: String,
    // 플립 이름(닉네임)
    val nickname: String,
    val introduce: String,
    val photoUrl: String,
    val postCnt: Int,
    val followerCnt: Int,
    val followingCnt: Int,
    val categories: List<Int>,
    val rating: String,
)
