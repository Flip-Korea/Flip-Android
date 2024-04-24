package com.team.domain.model.post

import com.team.domain.model.profile.DisplayProfile

data class Post(
    val postId: Long,
    val profile: DisplayProfile,
    val title: String,
    val content: String,
    val liked: Boolean,
    val likeCnt: Long,
    val commentCnt: Long,
    val scrapCnt: Long,
    val scraped: Boolean,
    val scrapComment: String?,
    val categoryId: Int,
    val bgColorId: Int,
    val fontStyleId: Int,
    val tag: List<String>?,
    val createdAt: String,
)