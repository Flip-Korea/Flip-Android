package com.team.domain.usecase.post.testdoubles

import com.team.domain.model.post.Post
import com.team.domain.model.post.PostList
import com.team.domain.model.profile.DisplayProfile

fun getPostListTestData(count: Int = 15): PostList {
    val posts = mutableListOf<Post>()
    repeat(count) {
        posts.add(
            Post(
                postId = 0L,
                profile = DisplayProfile("", "", ""),
                title = "title",
                content = "content",
                createdAt = "2024-07-10",
                liked = false,
                likeCnt = 10,
                commentCnt = 10,
                scraped = false,
                bgColorId = 1,
            )
        )
    }

    return PostList(
        hasNext = true,
        nextCursor = "20240618",
        postCnt = 15,
        posts = posts,
    )
}

fun getPostsTestData(count: Int = 15): List<Post> {
    val posts = mutableListOf<Post>()
    repeat(count) {
        posts.add(
            Post(
                postId = 0L,
                profile = DisplayProfile("", "", ""),
                title = "title",
                content = "content",
                createdAt = "2024-07-10",
                liked = false,
                likeCnt = 10,
                commentCnt = 10,
                scraped = false,
                bgColorId = 1,
            )
        )
    }

    return posts
}

