package com.team.data.network.source.fake

import com.team.data.network.model.request.CommentRequest
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.PostRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakePostNetworkDataSource(
    private val postNetworkApi: PostNetworkApi,
) : PostNetworkDataSource {

    override suspend fun getPosts(
        cursor: String?,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
        val result = postNetworkApi.getPosts(cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getPostById(postId: Long): Result<PostResponse, ErrorType> {
        val result = postNetworkApi.getPostById(postId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun addPost(postRequest: PostRequest): Result<Boolean, ErrorType> {
        val result = postNetworkApi.addPost(postRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
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
        cursor: String?,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
        val result = postNetworkApi.getPostsByType(type.asString(), typeId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
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
        cursor: String?,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
        val result = postNetworkApi.getPostsByPopularUser(categoryId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getComments(
        postId: Long,
        cursor: String?,
        limit: Int,
    ): Result<CommentListResponse, ErrorType> {
        val result = postNetworkApi.getComments(postId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
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

    override suspend fun addTemporaryPost(postRequest: PostRequest): Result<Boolean, ErrorType> {
        val result = postNetworkApi.addTemporaryPost(postRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
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
        cursor: String?,
        limit: Int,
    ): Result<TempPostListResponse, ErrorType> {
        val result = postNetworkApi.getTemporaryPosts(profileId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
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