package com.team.domain.usecase.profile.testdoubles

import com.team.domain.model.profile.MyProfile

val myProfileTestData: (String) -> MyProfile = { profileId ->
    MyProfile(
        profileId = profileId,
        nickname = "",
        introduce = "",
        photoUrl = "",
        postCnt = 0,
        followerCnt = 0,
        followingCnt = 0,
        categories = listOf(6, 7, 8),
        rating = "",
    )
}