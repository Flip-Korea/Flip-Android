package com.team.data.network.source

import com.team.data.network.model.request.CommentRequest
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.PostRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import com.team.data.network.networkCall
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class PostNetworkDataSourceImpl(
    private val postNetworkApi: PostNetworkApi
): PostNetworkDataSource {

    override suspend fun getPosts(
        cursor: String?,
        limit: Int,
    ): Result<PostListResponse, ErrorType> =
        networkCall { postNetworkApi.getPosts(cursor, limit) }

    override suspend fun getPostById(postId: Long): Result<PostResponse, ErrorType> =
        networkCall { postNetworkApi.getPostById(postId) }

    override suspend fun addPost(postRequest: PostRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.addPost(postRequest) }

    override suspend fun editPost(postRequest: PostRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.editPost(postRequest) }

    override suspend fun getPostsByType(
        type: PathParameterType,
        typeId: String,
        cursor: String?,
        limit: Int,
    ): Result<PostListResponse, ErrorType> =
        networkCall {
            postNetworkApi.getPostsByType(type.asString(), typeId, cursor, limit)
        }

    override suspend fun deletePost(postId: Long): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.deletePost(postId) }

    override suspend fun getPostsByPopularUser(
        categoryId: Int,
        cursor: String?,
        limit: Int,
    ): Result<PostListResponse, ErrorType> =
        networkCall { postNetworkApi.getPostsByPopularUser(categoryId, cursor, limit) }

    override suspend fun getComments(
        postId: Long,
        cursor: String?,
        limit: Int,
    ): Result<CommentListResponse, ErrorType> =
        networkCall { postNetworkApi.getComments(postId, cursor, limit) }

    override suspend fun addComment(
        postId: Long,
        commentRequest: CommentRequest,
    ): Result<ResultIdResponse, ErrorType> =
        networkCall { postNetworkApi.addComment(postId, commentRequest) }

    override suspend fun deleteComment(commentId: Long): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.deleteComment(commentId) }

    override suspend fun likePost(likeRequest: LikeRequest): Result<ResultIdResponse, ErrorType> =
        networkCall { postNetworkApi.likePost(likeRequest) }

    override suspend fun unLikePost(likeRequest: LikeRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.unLikePost(likeRequest) }

    override suspend fun addTemporaryPost(postRequest: PostRequest): Result<ResultIdResponse, ErrorType> =
        networkCall { postNetworkApi.addTemporaryPost(postRequest) }

    override suspend fun deleteTemporaryPost(tempPostId: Long): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.deleteTemporaryPost(tempPostId) }

    override suspend fun getTemporaryPosts(
        profileId: String,
        cursor: String?,
        limit: Int,
    ): Result<TempPostListResponse, ErrorType> =
        networkCall { postNetworkApi.getTemporaryPosts(profileId, cursor, limit) }

    override suspend fun editTemporaryPost(postRequest: PostRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { postNetworkApi.editTemporaryPost(postRequest) }
}