package com.team.data.local.entity.post

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.data.local.entity.profile.DisplayProfileEntity
import com.team.data.local.entity.profile.toEntity
import com.team.data.local.entity.profile.toDomainModel
import com.team.domain.model.post.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    val postId: Long,
    @Embedded(prefix = "profile_") val profile: DisplayProfileEntity,
    val title: String,
    val content: String,
    val liked: Boolean,
    val likeCnt: Long,
    val commentCnt: Long,
    val scrapCnt: Long,
    val scraped: Boolean,
    val scrapComment: String?,
    val categoryId: Int,
    val bgColorType: BackgroundColorTypeEntity,
    val fontStyleId: Int,
    val tag: List<String>?,
    val createdAt: String,
)

fun List<PostEntity>.toDomainModel(): List<Post> = this.map { it.toDomainModel() }

fun PostEntity.toDomainModel(): Post =
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
        fontStyleId = fontStyleId,
        tag = tag,
        createdAt = createdAt,
    )

fun Post.toEntity(): PostEntity =
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
        bgColorType = bgColorType.toEntity(),
        fontStyleId = fontStyleId,
        tag = tag,
        createdAt = createdAt,
    )

//fun NewPost.toEntity(displayProfile: DisplayProfile, postId: Long) =
//    PostEntity(
//        profile = displayProfile.toEntity(),
//        postId = postId,
//        title = title,
//        content = content,
//        createdAt = createdAt,
//        categoryId = categoryId,
//        bgColorId = bgColorId,
//        fontStyleId = fontStyleId,
//        tag = tags,
//        scrapComment = "",
//        scraped = false,
//        scrapCnt = 0,
//        commentCnt = 0,
//        likeCnt = 0,
//        liked = false
//    )