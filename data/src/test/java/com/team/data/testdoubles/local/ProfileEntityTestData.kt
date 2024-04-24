package com.team.data.testdoubles.local

import com.team.data.local.entity.profile.MyProfileEntity

fun makeProfileEntityTestData(profileId: String): MyProfileEntity =
    MyProfileEntity(
        profileId = profileId,
        nickname = "홍길동이(메인)",
        introduce = "홍길동 메인 계정입니당.",
        postCnt = 3,
        followingCnt = 10,
        followerCnt = 11,
        rating = "default",
        photoUrl = "https://test.com/123",
        categories = listOf(1, 2, 3)
    )