package com.team.domain.repository

import com.team.domain.model.post.NewPost
import com.team.domain.model.post.TempPost
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface TempPostRepository {

    fun getTempPostsPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<List<TempPost>, ErrorType>>

    fun addTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>

    fun deleteTemporaryPost(tempPostId: Long): Flow<Result<Boolean, ErrorType>>

    fun editTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>
}