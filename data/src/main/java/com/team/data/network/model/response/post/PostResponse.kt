package com.team.data.network.model.response.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.data.local.entity.post.PostEntity
import com.team.data.network.model.response.profile.DisplayProfileResponse
import com.team.data.network.model.response.profile.toEntity
import com.team.data.network.model.response.profile.toExternal
import com.team.domain.model.post.Post

@JsonClass(generateAdapter = true)
data class PostResponse(
    @Json(name = "post_id") val postId: Long,
    @Json(name = "profile") val profile: DisplayProfileResponse,
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "liked") val liked: Boolean,
    @Json(name = "like_cnt") val likeCnt: Long,
    @Json(name = "comment_cnt") val commentCnt: Long,
    @Json(name = "scrap_cnt") val scrapCnt: Long,
    @Json(name = "scraped") val scraped: Boolean,
    @Json(name = "scrap_comment") val scrapComment: String?,
    @Json(name = "category_id") val categoryId: Int,
    @Json(name = "bg_color_id") val bgColorId: Int,
    @Json(name = "font_style_id") val fontStyleId: Int,
    @Json(name = "tag") val tag: List<String>?,
    @Json(name = "created_at") val createdAt: String,
)

fun PostResponse.toEntity(): PostEntity =
    PostEntity(
        postId = postId,
        profile = profile.toEntity(),
        title = title,
        content = content,
        liked = liked,
        likeCnt = likeCnt,
        commentCnt = commentCnt,
        scrapCnt = scrapCnt,
        scraped = scraped,
        scrapComment = scrapComment,
        categoryId = categoryId,
        bgColorId = bgColorId,
        fontStyleId = fontStyleId,
        tag = tag,
        createdAt = createdAt,
    )

fun List<PostResponse>.toExternal(): List<Post> = this.map { it.toExternal() }

fun PostResponse.toExternal(): Post =
    Post(
        postId = postId,
        profile = profile.toExternal(),
        title = title,
        content = content,
        liked = liked,
        likeCnt = likeCnt,
        commentCnt = commentCnt,
        scrapCnt = scrapCnt,
        scraped = scraped,
        scrapComment = scrapComment,
        categoryId = categoryId,
        bgColorId = bgColorId,
        fontStyleId = fontStyleId,
        tag = tag,
        createdAt = createdAt,
    )