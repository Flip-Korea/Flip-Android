package com.team.data.repository

import com.team.data.di.IODispatcher
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.TempPostList
import com.team.domain.repository.TempPostRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultTempPostRepository @Inject constructor(
    private val postNetworkDataSource: PostNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): TempPostRepository {

    override fun getTempPostsPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<TempPostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getTemporaryPosts(profileId, cursor, limit)) {
            is Result.Success -> {
                val tempPosts = result.data.toDomainModel()
                emit(Result.Success(tempPosts))
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()

        when (val result =
            postNetworkDataSource.addTemporaryPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deleteTemporaryPost(tempPostId: Long): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.deleteTemporaryPost(tempPostId)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }

    override fun editTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()

        when (val result =
            postNetworkDataSource.editTemporaryPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}