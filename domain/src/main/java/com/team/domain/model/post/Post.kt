package com.team.domain.model.post

import com.team.domain.model.profile.DisplayProfile
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

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
    val bgColorType: BackgroundColorType,
    val fontStyleType: FontStyleType,
    val tags: List<String>?,
    val createdAt: String,
) {
    constructor(
        postId: Long,
        profile: DisplayProfile,
        title: String,
        content: String,
        createdAt: String,
        liked: Boolean,
        likeCnt: Long,
        commentCnt: Long,
        scraped: Boolean,
        bgColorType: BackgroundColorType
    ) : this(
        postId = postId,
        profile = profile,
        title = title,
        content = content,
        liked = liked,
        likeCnt = likeCnt,
        commentCnt = commentCnt,
        scrapCnt = 0L,
        scraped = scraped,
        scrapComment = null,
        categoryId = 0,
        bgColorType = bgColorType,
        fontStyleType = FontStyleType.NORMAL,
        tags = emptyList(),
        createdAt = createdAt
    )
}