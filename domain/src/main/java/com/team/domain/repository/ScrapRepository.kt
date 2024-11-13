package com.team.domain.repository

import com.team.domain.model.post.PostList
import com.team.domain.model.scrap.NewScrap
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ScrapRepository {

    fun getScrapListPagination(
        profileId: String,
        cursor: String,
        limit: Int
    ): Flow<Result<PostList, ErrorType>>

    fun editScrapComment(
        profileId: String,
        scrapId: Long,
        scrapComment: String
    ): Flow<Result<Boolean, ErrorType>>

    fun addScrap(newScrap: NewScrap): Flow<Result<Boolean, ErrorType>>

    fun deleteScrap(scrapId: Long): Flow<Result<Boolean, ErrorType>>
}