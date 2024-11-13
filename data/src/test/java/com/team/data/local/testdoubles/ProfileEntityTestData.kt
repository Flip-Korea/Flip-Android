package com.team.data.local.testdoubles

import com.team.data.local.entity.profile.MyProfileEntity

fun makeMyProfileEntityTestData(profileId: String): MyProfileEntity =
    MyProfileEntity(
        profileId = profileId,
        nickname = "홍길동이(메인)",
        introduce = "makeMyProfileEntityTestData",
        postCnt = 3,
        followingCnt = 10,
        followerCnt = 11,
        rating = "default",
        photoUrl = "https://test.com/123",
        categories = listOf(1, 2, 3)
    )