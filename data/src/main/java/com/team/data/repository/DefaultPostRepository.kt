package com.team.data.repository

import com.team.data.di.ApplicationScope
import com.team.data.di.IODispatcher
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.Post
import com.team.domain.model.post.PostList
import com.team.domain.repository.PostRepository
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultPostRepository @Inject constructor(
    private val postNetworkDataSource: PostNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    /**
     * 앱의 수명 주기를 따르는 범위 삽입,
     * 앱의 수명 주기를 따르는 이유:
     * 앱 실행 중 단 하나의 코루틴 스코프를 생성하고 유지하도록 Module에서 관리
     */
    @ApplicationScope private val scope: CoroutineScope,
) : PostRepository {

    override fun getPostsPagination(cursor: String?, limit: Int): Flow<Result<PostList, ErrorType>> {
        return flow {
            emit(Result.Loading)

            when (val result = postNetworkDataSource.getPosts(cursor, limit)) {
                is Result.Success -> {
                    val postList = result.data.toDomainModel()
                    emit(Result.Success(postList))
                }
                is Result.Error -> {
                    emit(Result.Error(errorBody = result.errorBody, error = result.error))
                }
                Result.Loading -> { }
            }
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    override fun getPostById(postId: Long): Flow<Result<Post?, ErrorType>> = flow<Result<Post?, ErrorType>> {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.getPostById(postId)) {
            is Result.Success -> {
                val post = result.data.toDomainModel()
                emit(Result.Success(post))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()
        when (val result = postNetworkDataSource.addPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun editPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()
        when (val result = postNetworkDataSource.editPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getPostsByTypePagination(
        type: PathParameterType,
        typeId: String,
        cursor: String?,
        limit: Int
    ): Flow<Result<PostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getPostsByType(type, typeId, cursor, limit)) {
            is Result.Success -> {
                val postList = result.data.toDomainModel()
                emit(Result.Success(postList))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deletePost(postId: Long): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.deletePost(postId)) {
            is Result.Success -> { emit(Result.Success(result.data)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getPostsByPopularUserPagination(
        categoryId: Int,
        cursor: String?,
        limit: Int,
    ): Flow<Result<PostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getPostsByPopularUser(categoryId, cursor, limit)) {
            is Result.Success -> {
                val postList = result.data.toDomainModel()
                emit(Result.Success(postList))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun likePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val likeResult = LikeRequest(profileId, postId)
        when (val result = postNetworkDataSource.likePost(likeResult)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }

    override fun unLikePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val likeResult = LikeRequest(profileId, postId)
        when (val result = postNetworkDataSource.unLikePost(likeResult)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
}