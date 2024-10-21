package com.team.data.network.model.response.post

import com.squareup.moshi.JsonClass
import com.team.data.network.model.response.profile.DisplayProfileResponse
import com.team.data.network.model.response.profile.toDomainModel
import com.team.domain.model.post.Post

@JsonClass(generateAdapter = true)
data class PostResponse(
    val postId: Long,
    val profile: DisplayProfileResponse,
    val title: String,
    val content: String,
    val liked: Boolean,
    val likeCnt: Long,
    val commentCnt: Long,
    val scrapCnt: Long,
    val scraped: Boolean,
    val scrapComment: String?,
    val categoryId: Int,
    val bgColorType: BackgroundColorTypeResponse,
    val fontStyleType: FontStyleTypeResponse,
    val tags: List<String>?,
    val postAt: String,
)

fun List<PostResponse>.toDomainModel(): List<Post> = this.map { it.toDomainModel() }

fun PostResponse.toDomainModel(): Post =
    Post(
        postId = postId,
        profile = profile.toDomainModel(),
        title = title,
        content = content,
        liked = liked,
        likeCnt = likeCnt,
        commentCnt = commentCnt,
        scrapCnt = scrapCnt,
        scraped = scraped,
        scrapComment = scrapComment,
        categoryId = categoryId,
        bgColorType = bgColorType.toDomainModel(),
        fontStyleType = fontStyleType.toDomainModel(),
        tags = tags,
        createdAt = postAt,
    )