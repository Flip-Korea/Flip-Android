package com.team.domain.repository

import com.team.domain.model.RecentSearch
import com.team.domain.model.post.PostList
import com.team.domain.model.profile.DisplayProfileList
import com.team.domain.model.tag.TagResultList
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecentSearchList(): Flow<List<RecentSearch>>

    suspend fun deleteRecentSearchById(id: Long): Boolean

    suspend fun deleteAllRecentSearch(): Boolean

    fun searchByPostPagination(
        searchQuery: String,
        cursor: String,
        limit: Int
    ): Flow<Result<PostList, ErrorType>>

    fun searchByNicknamePagination(
        searchQuery: String,
        cursor: String,
        limit: Int
    ): Flow<Result<DisplayProfileList, ErrorType>>

    fun searchByTagPagination(
        searchQuery: String,
        cursor: String,
        limit: Int
    ): Flow<Result<TagResultList, ErrorType>>
}