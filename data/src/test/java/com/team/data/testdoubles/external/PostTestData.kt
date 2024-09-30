package com.team.data.testdoubles.external

import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.type.BackgroundColorType

fun makePostTestData(postId: Long): Post =
    Post(
        postId = postId,
        profile = DisplayProfile(
            profileId = "test-profile-123",
            nickname = "test-nickname",
            photoUrl = "https://test.com/123",
            isFollower = false,
            isFollowing = false,
            introduce = null,
            followerCnt = 30,
            rating = null
        ),
        title = "테스트 타이틀",
        content = "테스트 내용",
        liked = false,
        likeCnt = 22,
        commentCnt = 3,
        scrapCnt = 2,
        scraped = false,
        scrapComment = "테스트 스크랩 코멘트",
        categoryId = 1,
        bgColorType = BackgroundColorType.BLUE,
        fontStyleId = 1,
        tag = listOf("테스트 태그1", "테스트 태그2"),
        createdAt = "2024-04-11"
    )