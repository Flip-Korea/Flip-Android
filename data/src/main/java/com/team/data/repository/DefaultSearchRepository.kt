package com.team.data.repository

import com.team.data.di.IODispatcher
import com.team.data.local.dao.RecentSearchDao
import com.team.data.local.entity.RecentSearchEntity
import com.team.data.local.entity.toDomainModel
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.model.response.profile.toDomainModel
import com.team.data.network.model.response.tag.toDomainModel
import com.team.data.network.source.SearchNetworkDataSource
import com.team.domain.model.RecentSearch
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.model.tag.TagResult
import com.team.domain.repository.SearchRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class DefaultSearchRepository @Inject constructor(
    private val searchNetworkDataSource: SearchNetworkDataSource,
    private val recentSearchDao: RecentSearchDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): SearchRepository {

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
    ): Flow<Result<List<Post>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = searchNetworkDataSource.searchByPost(searchQuery, cursor, limit)) {
            is Result.Success -> {
                recentSearchDao.upsertRecentSearch(RecentSearchEntity(word = searchQuery))

                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
                    val posts = withContext(ioDispatcher) {
                        result.data.posts.toDomainModel()
                    }
                    emit(Result.Success(posts))
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


    override fun searchByNicknamePagination(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<DisplayProfile>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = searchNetworkDataSource.searchByNickname(searchQuery, cursor, limit)) {
            is Result.Success -> {
                recentSearchDao.upsertRecentSearch(RecentSearchEntity(word = searchQuery))

                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
                    val profiles = withContext(ioDispatcher) {
                        result.data.profiles.toDomainModel()
                    }
                    emit(Result.Success(profiles))
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

    override fun searchByTagPagination(
        searchQuery: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<TagResult>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = searchNetworkDataSource.searchByTag(searchQuery, cursor, limit)) {
            is Result.Success -> {
                recentSearchDao.upsertRecentSearch(RecentSearchEntity(word = searchQuery))

                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
                    val tags = withContext(ioDispatcher) {
                        result.data.tags.toDomainModel()
                    }
                    emit(Result.Success(tags))
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
}