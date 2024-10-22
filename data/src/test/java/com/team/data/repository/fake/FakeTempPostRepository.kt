package com.team.data.repository.fake

import androidx.paging.PagingData
import androidx.paging.map
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.network.testdoubles.factory.TempPostResponseFactory
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.TempPost
import com.team.domain.model.post.TempPostList
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
    private val pageSize: Int,
) : TempPostRepository {

    private val ioDispatcher = Dispatchers.IO
    private val tempPostResponseFactory = TempPostResponseFactory()

    override fun getTempPostsPagination(): Flow<PagingData<TempPost>> = flow {
        val tempPosts = List(pageSize) {
            tempPostResponseFactory
                .create()
                .toDomainModel()
        }
        val tempPostPagingData = PagingData.from(tempPosts)

        emit(tempPostPagingData)
    }

    override fun addTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()

        when (val result =
            postNetworkDataSource.addTemporaryPost(newPostNetwork)) {
            is Result.Success -> {
                emit(Result.Success(true))
            }

            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }

            Result.Loading -> {}
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deleteTemporaryPost(tempPostId: Long): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.deleteTemporaryPost(tempPostId)) {
            is Result.Success -> {
                emit(Result.Success(true))
            }

            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }

            Result.Loading -> {}
        }
    }

    override fun editTemporaryPost(
        tempPostId: Long,
        newPost: NewPost
    ): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()

        when (val result =
            postNetworkDataSource.editTemporaryPost(tempPostId, newPostNetwork)) {
            is Result.Success -> {
                emit(Result.Success(true))
            }

            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }

            Result.Loading -> {}
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}