package com.team.data.network.source

import com.team.data.network.model.request.CommentRequest
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.PostRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface PostNetworkDataSource {
    suspend fun getPosts(cursor: String?, limit: Int): Result<PostListResponse, ErrorType>

    suspend fun getPostById(postId: Long): Result<PostResponse, ErrorType>

    suspend fun addPost(postRequest: PostRequest): Result<Boolean, ErrorType>

    suspend fun editPost(postRequest: PostRequest): Result<Boolean, ErrorType>

    suspend fun getPostsByType(
        type: PathParameterType,
        typeId: String,
        cursor: String?,
        limit: Int
    ): Result<PostListResponse, ErrorType>
    suspend fun deletePost(postId: Long): Result<Boolean, ErrorType>
    suspend fun getPostsByPopularUser(
        categoryId: Int,
        cursor: String?,
        limit: Int
    ): Result<PostListResponse, ErrorType>
    suspend fun getComments(
        postId: Long,
        cursor: String?,
        limit: Int
    ): Result<CommentListResponse, ErrorType>
    suspend fun addComment(
        postId: Long,
        commentRequest: CommentRequest
    ): Result<ResultIdResponse, ErrorType>
    suspend fun deleteComment(commentId: Long): Result<Boolean, ErrorType>
    suspend fun likePost(likeRequest: LikeRequest): Result<ResultIdResponse, ErrorType>
    suspend fun unLikePost(likeRequest: LikeRequest): Result<Boolean, ErrorType>
    suspend fun addTemporaryPost(postRequest: PostRequest): Result<Boolean, ErrorType>
    suspend fun deleteTemporaryPost(tempPostId: Long): Result<Boolean, ErrorType>
    suspend fun getTemporaryPosts(
        profileId: String,
        cursor: String?,
        limit: Int
    ): Result<TempPostListResponse, ErrorType>
    suspend fun editTemporaryPost(postRequest: PostRequest): Result<Boolean, ErrorType>
}