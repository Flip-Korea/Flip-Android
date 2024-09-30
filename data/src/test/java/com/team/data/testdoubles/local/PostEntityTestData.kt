package com.team.data.testdoubles.local

import com.team.data.local.entity.post.BackgroundColorTypeEntity
import com.team.data.local.entity.profile.DisplayProfileEntity
import com.team.data.local.entity.post.PostEntity
import kotlin.random.Random

fun makePostEntityTestData(postId: Long): PostEntity =
    PostEntity(
        postId = postId,
        profile = DisplayProfileEntity(
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
        bgColorType = BackgroundColorTypeEntity.BLUE,
        fontStyleId = 1,
        tag = listOf("테스트 태그1", "테스트 태그2"),
        createdAt = "2024-04-11"
    )

fun makeMultiplePostEntityTestData(postIds: List<Long>): List<PostEntity> {
    val list = mutableListOf<PostEntity>()

    postIds.forEach { postId ->
        list.add(
            PostEntity(
                postId = postId,
                profile = DisplayProfileEntity(
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
                bgColorType = BackgroundColorTypeEntity.BLUE,
                fontStyleId = 1,
                tag = listOf("테스트 태그1", "테스트 태그2"),
                createdAt = "2024-04-11"
            )
        )
    }

    return list
}

fun makePostIds(count: Int): List<Long> {
    val list = mutableListOf<Long>()

    repeat(count) {
        list.add(Random.nextLong(1, 50000))
    }

    return list
}
