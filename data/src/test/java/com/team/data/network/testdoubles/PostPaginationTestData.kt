import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.comment.CommentResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import com.team.data.network.model.response.post.TempPostResponse
import com.team.data.network.model.response.profile.DisplayProfileResponse
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.domain.type.PathParameterType
import kotlin.random.Random

/** 총 3페이지만 반환 **/
fun makePostListResponseTestData(
    cursor: String,
    pageSize: Int,
    type: PathParameterType = PathParameterType.Post.CATEGORY,
    typeId: String = "1"
): String {

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
                    profileId = "tp",
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
                categoryId = typeId.toInt(),
                bgColorId = 1,
                fontStyleId = 1,
                tag = listOf("1","2"),
                createdAt = cursor
            )
        )
    }

    val postListResponse = PostListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        postCnt = pageSize.toLong(),
        posts = list
    )

    val json = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter(PostListResponse::class.java)
        .toJson(postListResponse)

    return json
}

/** 총 3페이지만 반환 **/
fun makeCommentListResponseTestData(
    postId: Long,
    cursor: String,
    pageSize: Int,
): String {

    val commentIds = mutableListOf<Long>()
    repeat(pageSize) {
        commentIds.add(Random.nextLong(1, 50000))
    }
    val list = mutableListOf<CommentResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    commentIds.forEachIndexed{ index, commentId ->
        list.add(
            CommentResponse(
                commentId = commentId,
                profileId = "TestProfileId",
                nickname = "TestNickname",
                photoUrl = "https://test.com/123",
                content = "테스트 댓글",
                commentDate = "2024-04-23"
            )
        )
    }

    val commentListResponse = CommentListResponse(
        hasNext = hasNext,
        nextCursor = nextCursor,
        commentCnt = pageSize,
        comments = list
    )

    val json = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter(CommentListResponse::class.java)
        .toJson(commentListResponse)

    return json
}

/** 총 3페이지만 반환 **/
fun makeTempPostListResponseTestData(
    cursor: String,
    pageSize: Int,
): String {

    val list = mutableListOf<TempPostResponse>()

    val hasNext = cursor != "4"
    val nextCursor = (cursor.toInt()+1).toString()

    repeat(pageSize) { index ->
        list.add(
            TempPostResponse(
                title = "TestTitle($index)",
                content = "TestContent($index)",
                categoryId = 1,
                bgColorType = BackgroundColorType.DEFAULT,
                fontStyleType = FontStyleType.NORMAL,
                tags = listOf("1","2"),
                postAt = "2024-09-08",
                categoryName = "일상",
                tempPostId = index.toLong()
            )
        )
    }

    val tempPostListResponse = TempPostListResponse(
        tempPosts = list,
        totalCount = pageSize
    )

    val json = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter(TempPostListResponse::class.java)
        .toJson(tempPostListResponse)

    return json
}