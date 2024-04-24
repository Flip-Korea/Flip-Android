package com.team.data.repository.fake

import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toExternal
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.TempPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeTempPostRepository(
    private val postNetworkDataSource: PostNetworkDataSource,
): TempPostRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun getTempPostsPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<TempPost>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getTemporaryPosts(profileId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
//                    val tempPosts = withContext(ioDispatcher) {
//                        result.data.tempPosts.toExternal()
//                    }
                    val tempPosts = result.data.tempPosts.toExternal()
                    emit(Result.Success(tempPosts))
                } else {
                    emit(Result.Success(emptyList()))
                }
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

//        val newPostNetwork = withContext(ioDispatcher) { newPost.toNetwork() }

        when (val result =
            postNetworkDataSource.addTemporaryPost(newPost.toNetwork())) {
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

//        val newPostNetwork = withContext(ioDispatcher) { newPost.toNetwork() }

        when (val result =
            postNetworkDataSource.editTemporaryPost(newPost.toNetwork())) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}