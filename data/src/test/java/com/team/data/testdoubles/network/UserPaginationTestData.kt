package com.team.data.testdoubles.network

import com.team.data.network.model.response.block.BlockListResponse
import com.team.data.network.model.response.block.BlockProfileResponse
import com.team.data.network.model.response.comment.DisplayPostResponse
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.profile.DisplayProfileResponse
import kotlin.random.Random

/** 총 3페이지만 반환 **/
fun makeScrapListResponseTestData(
    profileId: String,
    cursor: String,
    pageSize: Int,
): PostListResponse {

    val postIds = mutableListOf<Long>()
    repeat(pageSize) {
        postIds.add(Random.nextLong(1, 50000))
    }
    val list = mutableListOf<PostResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    postIds.forEachIndexed{ index, postId ->
        list.add(
            PostResponse(
                postId = postId,
                profile = DisplayProfileResponse(
                    profileId = profileId,
                    nickname = "nickname",
                    photoUrl = "https://test.com/123",
                    isFollower = false,
                    isFollowing = false,
                    introduce = null,
                    followerCnt = 30,
                    rating = null
                ),
                title = "TestTitle($index)",
                content = "TestContent($index)",
                liked = false,
                likeCnt = 10,
                commentCnt = 10,
                scrapCnt = 10,
                scraped = false,
                scrapComment = "My Scrap",
                categoryId = 1,
                bgColorId = 1,
                fontStyleId = 1,
                tag = listOf("1","2"),
                createdAt = cursor
            )
        )
    }

    return PostListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        postCnt = pageSize.toLong(),
        posts = list
    )
}

/** 총 3페이지만 반환 **/
fun makeFollowerListResponseTestData(
    profileId: String,
    cursor: String,
    pageSize: Int,
): FollowerListResponse {

    val profileIds = mutableListOf<Long>()
    repeat(pageSize) {
        profileIds.add(Random.nextLong(1, 50000))
    }
    val list = mutableListOf<DisplayProfileResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    profileIds.forEachIndexed{ index, profildId ->
        list.add(
            DisplayProfileResponse(
                profileId = profildId.toString(),
                nickname = "nickname",
                photoUrl = "https://test.com/123",
                isFollower = false,
                isFollowing = false,
                introduce = null,
                followerCnt = 30,
                rating = null
            )
        )
    }

    return FollowerListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        followers = list
    )
}

/** 총 3페이지만 반환 **/
fun makeFollowingListResponseTestData(
    profileId: String,
    cursor: String,
    pageSize: Int,
): FollowingListResponse {

    val profileIds = mutableListOf<Long>()
    repeat(pageSize) {
        profileIds.add(Random.nextLong(1, 50000))
    }
    val list = mutableListOf<DisplayProfileResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    profileIds.forEachIndexed{ index, profildId ->
        list.add(
            DisplayProfileResponse(
                profileId = profildId.toString(),
                nickname = "nickname",
                photoUrl = "https://test.com/123",
                isFollower = false,
                isFollowing = false,
                introduce = null,
                followerCnt = 30,
                rating = null
            )
        )
    }

    return FollowingListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        followings = list
    )
}

/** 총 3페이지만 반환 **/
fun makeBlockListResponseTestData(
    profileId: String,
    cursor: String,
    pageSize: Int,
): BlockListResponse {

    val profileIds = mutableListOf<Long>()
    repeat(pageSize) {
        profileIds.add(Random.nextLong(1, 50000))
    }
    val list = mutableListOf<BlockProfileResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    profileIds.forEachIndexed{ index, profildId ->
        list.add(
            BlockProfileResponse(
                blockId = 322L,
                blockedId = "honggd",
                nickname = "nickname",
                photoUrl = "https://test.com/123",
            )
        )
    }

    return BlockListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        blockList = list
    )
}

/** 총 3페이지만 반환 **/
fun makeMyCommentListResponseTestData(
    profileId: String,
    cursor: String,
    pageSize: Int,
): MyCommentListResponse {

    val postIds = mutableListOf<Long>()
    repeat(pageSize) {
        postIds.add(Random.nextLong(1, 50000))
    }
    val list = mutableListOf<DisplayPostResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    postIds.forEachIndexed{ index, postId ->
        list.add(
            DisplayPostResponse(
                postId = postId,
                title = "title",
                nickname = "nickname",
                myComment = "asdasdasda",
                createdAt = "2024-04-25"
            )
        )
    }

    return MyCommentListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        posts = list
    )
}