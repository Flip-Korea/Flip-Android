package com.team.data.network.source.fake

import com.team.data.network.model.request.CommentRequest
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.PostRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.comment.CommentResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import com.team.data.network.model.response.post.TempPostResponse
import com.team.data.network.model.response.profile.DisplayProfileResponse
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlin.random.Random

class FakePostNetworkDataSource(
    private val postNetworkApi: PostNetworkApi,
) : PostNetworkDataSource {

    /** 총 3페이지만 반환 **/
    private fun makePostListResponseTestData(
        cursor: String,
        pageSize: Int,
        type: PathParameterType = PathParameterType.Post.CATEGORY,
        typeId: String = "1"
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
                        profileId = "tp",
                        nickname = "nickname",
                        photoUrl = "https://test.com/123",
                        following = false,
                        followerCnt = null
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

        return PostListResponse(
            hasNext = hasNext,
            nextCursor = nextCursor,
            postCnt = pageSize.toLong(),
            posts = list
        )
    }

    /** 총 3페이지만 반환 **/
    private fun makeCommentListResponseTestData(
        postId: Long,
        cursor: String,
        pageSize: Int,
    ): CommentListResponse {

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

        return CommentListResponse(
            hasNext = hasNext,
            nextCursor = nextCursor,
            commentCnt = pageSize,
            comments = list
        )
    }

    /** 총 3페이지만 반환 **/
    private fun makeTempPostListResponseTestData(
        cursor: String,
        pageSize: Int,
    ): TempPostListResponse {

        val list = mutableListOf<TempPostResponse>()

        val hasNext = cursor != "4"
        val nextCursor = (cursor.toInt()+1).toString()

        repeat(pageSize) { index ->
            list.add(
                TempPostResponse(
                    profileId = "TestProfileId",
                    title = "TestTitle($index)",
                    content = "TestContent($index)",
                    categoryId = 1,
                    bgColorId = 1,
                    fontStyleId = 1,
                    tags = listOf("1","2"),
                    createdAt = cursor
                )
            )
        }

        return TempPostListResponse(
            hasNext = hasNext,
            nextCursor = nextCursor,
            tempPostCnt = pageSize,
            tempPosts = list
        )
    }

    override suspend fun getPosts(
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
//        val result = postNetworkApi.getPosts(cursor, limit)
        val postListResponse = makePostListResponseTestData(cursor, limit)
        return Result.Success(postListResponse)
    }

    override suspend fun getPostById(postId: Long): Result<PostResponse, ErrorType> {
        val result = postNetworkApi.getPostById(postId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun addPost(postRequest: PostRequest): Result<ResultIdResponse, ErrorType> {
        val result = postNetworkApi.addPost(postRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun editPost(postRequest: PostRequest): Result<Boolean, ErrorType> {
        val result = postNetworkApi.editPost(postRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getPostsByType(
        type: PathParameterType,
        typeId: String,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
//        val result = postNetworkApi.getPostsByType(type.asString(), typeId, cursor, limit)
        val postListResponse = makePostListResponseTestData(cursor, limit, type, typeId)
        return Result.Success(postListResponse)
    }

    override suspend fun deletePost(postId: Long): Result<Boolean, ErrorType> {
        val result = postNetworkApi.deletePost(postId)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getPostsByPopularUser(
        categoryId: Int,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
//        val result = postNetworkApi.getPostsByPopularUser(categoryId, cursor, limit)
        val postListResponse = makePostListResponseTestData(cursor, limit, typeId = categoryId.toString())
        return Result.Success(postListResponse)
    }

    override suspend fun getComments(
        postId: Long,
        cursor: String,
        limit: Int,
    ): Result<CommentListResponse, ErrorType> {
//        val result = postNetworkApi.getComments(postId, cursor, limit)
        val commentListResponse = makeCommentListResponseTestData(postId, cursor, limit)
        return Result.Success(commentListResponse)
    }

    override suspend fun addComment(
        postId: Long,
        commentRequest: CommentRequest,
    ): Result<ResultIdResponse, ErrorType> {
        val result = postNetworkApi.addComment(postId, commentRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun deleteComment(commentId: Long): Result<Boolean, ErrorType> {
        val result = postNetworkApi.deleteComment(commentId)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun likePost(likeRequest: LikeRequest): Result<ResultIdResponse, ErrorType> {
        val result = postNetworkApi.likePost(likeRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun unLikePost(likeRequest: LikeRequest): Result<Boolean, ErrorType> {
        val result = postNetworkApi.unLikePost(likeRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun addTemporaryPost(postRequest: PostRequest): Result<ResultIdResponse, ErrorType> {
        val result = postNetworkApi.addTemporaryPost(postRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun deleteTemporaryPost(tempPostId: Long): Result<Boolean, ErrorType> {
        val result = postNetworkApi.deleteTemporaryPost(tempPostId)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getTemporaryPosts(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<TempPostListResponse, ErrorType> {
//        val result = postNetworkApi.getTemporaryPosts(profileId, cursor, limit)
        val tempPosts = makeTempPostListResponseTestData(cursor, limit)
        return Result.Success(tempPosts)
    }

    override suspend fun editTemporaryPost(postRequest: PostRequest): Result<Boolean, ErrorType> {
        val result = postNetworkApi.editTemporaryPost(postRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}