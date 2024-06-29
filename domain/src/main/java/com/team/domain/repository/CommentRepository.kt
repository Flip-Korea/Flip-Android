package com.team.domain.repository

import com.team.domain.model.comment.CommentList
import com.team.domain.model.comment.NewComment
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCommentsPagination(
        postId: Long,
        cursor: String,
        limit: Int
    ): Flow<Result<CommentList, ErrorType>>

    fun addComment(postId: Long, newComment: NewComment): Flow<Result<Boolean, ErrorType>>

    fun deleteComment(commentId: Long): Flow<Result<Boolean, ErrorType>>
}