package com.team.data.repository

import com.team.data.di.ApplicationScope
import com.team.data.di.IODispatcher
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.comment.toExternal
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.comment.Comment
import com.team.domain.model.comment.NewComment
import com.team.domain.repository.CommentRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultCommentRepository @Inject constructor(
    private val postNetworkDataSource: PostNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
): CommentRepository {

    override fun getCommentsPagination(
        postId: Long,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<Comment>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getComments(postId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
                    val comments = withContext(ioDispatcher) {
                        result.data.comments.toExternal()
                    }
                    emit(Result.Success(comments))
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

    override fun addComment(
        postId: Long,
        newComment: NewComment,
    ): Flow<Result<Boolean, ErrorType>> = flow<Result<Boolean, ErrorType>> {
        emit(Result.Loading)

        val commentRequest = withContext(ioDispatcher) {
            newComment.toNetwork()
        }

        when (val result =
            postNetworkDataSource.addComment(postId = postId, commentRequest = commentRequest)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deleteComment(commentId: Long): Flow<Result<Boolean, ErrorType>> = flow<Result<Boolean, ErrorType>> {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.deleteComment(commentId)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}