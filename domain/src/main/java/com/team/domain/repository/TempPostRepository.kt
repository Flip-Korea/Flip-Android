package com.team.domain.repository

import androidx.paging.PagingData
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.TempPost
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface TempPostRepository {

    fun getTempPostsPagination(): Flow<PagingData<TempPost>>

    fun addTemporaryPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>>

    fun deleteTemporaryPost(tempPostId: Long): Flow<Result<Boolean, ErrorType>>

    fun editTemporaryPost(tempPostId: Long, newPost: NewPost): Flow<Result<Boolean, ErrorType>>
}