package com.team.data.repository.fake

import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.source.UserNetworkDataSource
import com.team.domain.model.post.Post
import com.team.domain.model.scrap.NewScrap
import com.team.domain.repository.ScrapRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class FakeScrapRepository(
    private val userNetworkDataSource: UserNetworkDataSource
): ScrapRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun getScrapListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<Post>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getScrapList(profileId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
//                    val scrapList = withContext(ioDispatcher) {
//                        result.data.posts.toExternal()
//                    }
                    val scrapList = result.data.posts.toDomainModel()
                    emit(Result.Success(scrapList))
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

    override fun editScrapComment(
        profileId: String,
        scrapId: Long,
        scrapComment: String,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.editScrapComment(profileId, scrapId, ScrapCommentRequest(scrapComment))) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addScrap(newScrap: NewScrap): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

//        val newScrapNetwork = withContext(ioDispatcher) { newScrap.toNetwork() }
        val newScrapNetwork = newScrap.toNetwork()

        when (val result =
            userNetworkDataSource.addScrap(newScrapNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deleteScrap(scrapId: Long): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.deleteScrap(scrapId)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}