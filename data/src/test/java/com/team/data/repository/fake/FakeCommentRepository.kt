package com.team.data.repository.fake

import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.comment.toDomainModel
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.comment.CommentList
import com.team.domain.model.comment.NewComment
import com.team.domain.repository.CommentRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeCommentRepository(
    private val postNetworkDataSource: PostNetworkDataSource
): CommentRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun getCommentsPagination(
        postId: Long,
        cursor: String,
        limit: Int,
    ): Flow<Result<CommentList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getComments(postId, cursor, limit)) {
            is Result.Success -> {
                val comments = result.data.toDomainModel()
                emit(Result.Success(comments))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addComment(
        postId: Long,
        newComment: NewComment,
    ): Flow<Result<Boolean, ErrorType>> = flow<Result<Boolean, ErrorType>> {
        emit(Result.Loading)

        val commentRequest = newComment.toNetwork()

        when (val result =
            postNetworkDataSource.addComment(postId = postId, commentRequest = commentRequest)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deleteComment(commentId: Long): Flow<Result<Boolean, ErrorType>> = flow<Result<Boolean, ErrorType>> {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.deleteComment(commentId)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}