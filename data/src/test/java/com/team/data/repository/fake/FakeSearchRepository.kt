package com.team.data.repository.fake

import com.team.data.local.dao.RecentSearchDao
import com.team.data.local.entity.RecentSearchEntity
import com.team.data.local.entity.toExternal
import com.team.data.network.model.response.post.toExternal
import com.team.data.network.model.response.profile.toExternal
import com.team.data.network.model.response.tag.toExternal
import com.team.data.network.source.SearchNetworkDataSource
import com.team.domain.model.RecentSearch
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.model.tag.TagResult
import com.team.domain.repository.SearchRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class FakeSearchRepository(
    private val searchNetworkDataSource: SearchNetworkDataSource,
    private val recentSearchDao: RecentSearchDao,
): SearchRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun getRecentSearchList(): Flow<List<RecentSearch>> =
        recentSearchDao.getRecentSearchList()
            .map { it.toExternal() }
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
//                    val posts = withContext(ioDispatcher) {
//                        result.data.posts.toExternal()
//                    }
                    val posts = result.data.posts.toExternal()
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
//                    val profiles = withContext(ioDispatcher) {
//                        result.data.profiles.toExternal()
//                    }
                    val profiles = result.data.profiles.toExternal()
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
//                    val tags = withContext(ioDispatcher) {
//                        result.data.tags.toExternal()
//                    }
                    val tags = result.data.tags.toExternal()
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