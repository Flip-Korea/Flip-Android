package com.team.data.repository.fake

import com.team.data.local.dao.RecentSearchDao
import com.team.data.local.entity.RecentSearchEntity
import com.team.data.local.entity.toDomainModel
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.model.response.profile.toDomainModel
import com.team.data.network.model.response.tag.toDomainModel
import com.team.data.network.source.SearchNetworkDataSource
import com.team.domain.model.RecentSearch
import com.team.domain.model.post.PostList
import com.team.domain.model.profile.DisplayProfileList
import com.team.domain.model.tag.TagResultList
import com.team.domain.repository.SearchRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException

class FakeSearchRepository(
    private val searchNetworkDataSource: SearchNetworkDataSource,
    private val recentSearchDao: RecentSearchDao,
): SearchRepository {

    private val ioDispatcher = Dispatchers.IO
    override fun getRecentSearchList(): Flow<List<RecentSearch>> =
        recentSearchDao.getRecentSearchList()
            .map { it.toDomainModel() }
            .catch { emit(emptyList()) }

    override suspend fun deleteRecentSearchById(id: Long): Boolean {
        return try {
            recentSearchDao.deleteById(id)
            true
        } catch (e: NullPointerException) { false }
        catch (e: IOException) { false }
    }

    override suspend fun deleteAllRecentSearch(): Boolean {
        return try {
            recentSearchDao.clearAll()
            true
        } catch (e: IOException) { false }
    }

    override fun searchByPostPagination(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<PostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = searchNetworkDataSource.searchByPost(searchQuery, cursor, limit)) {
            is Result.Success -> {
                recentSearchDao.upsertRecentSearch(RecentSearchEntity(word = searchQuery))
                val posts = result.data.toDomainModel()
                emit(Result.Success(posts))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }


    override fun searchByNicknamePagination(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayProfileList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = searchNetworkDataSource.searchByNickname(searchQuery, cursor, limit)) {
            is Result.Success -> {
                recentSearchDao.upsertRecentSearch(RecentSearchEntity(word = searchQuery))

                val profiles = result.data.toDomainModel()
                emit(Result.Success(profiles))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun searchByTagPagination(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<TagResultList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = searchNetworkDataSource.searchByTag(searchQuery, cursor, limit)) {
            is Result.Success -> {
                recentSearchDao.upsertRecentSearch(RecentSearchEntity(word = searchQuery))

                val tags = result.data.toDomainModel()
                emit(Result.Success(tags))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
}