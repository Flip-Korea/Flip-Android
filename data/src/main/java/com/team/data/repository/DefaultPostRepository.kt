package com.team.data.repository

import com.team.data.di.ApplicationScope
import com.team.data.di.IODispatcher
import com.team.data.local.dao.PostDao
import com.team.data.local.entity.post.toDomainModel
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toEntity
import com.team.data.network.model.response.post.toDomainModel
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.Post
import com.team.domain.model.post.PostList
import com.team.domain.repository.PostRepository
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultPostRepository @Inject constructor(
    private val postDao: PostDao,
    private val postNetworkDataSource: PostNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    /**
     * 앱의 수명 주기를 따르는 범위 삽입,
     * 앱의 수명 주기를 따르는 이유:
     * 앱 실행 중 단 하나의 코루틴 스코프를 생성하고 유지하도록 Module에서 관리
     */
    @ApplicationScope private val scope: CoroutineScope,
) : PostRepository {

    override fun getCachedPosts(): Flow<List<Post>> =
        postDao.getPosts()
            .map { it.toDomainModel() }
            .flowOn(ioDispatcher)
            .catch { emit(emptyList()) }

    override fun getPostsPagination(cursor: String?, limit: Int): Flow<Result<PostList, ErrorType>> {
        return flow {
            emit(Result.Loading)

            when (val result = postNetworkDataSource.getPosts(cursor, limit)) {
                is Result.Success -> {
                    if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
                        val postListEntities = result.data.posts.map { it.toEntity() }
                        postDao.refresh(postListEntities)
                    }
                    val postList = result.data.toDomainModel()
                    emit(Result.Success(postList))
                }
                is Result.Error -> { emit(Result.Error(result.error)) }
                Result.Loading -> { }
            }
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    override fun getPostById(postId: Long): Flow<Result<Post?, ErrorType>> = flow<Result<Post?, ErrorType>> {
        emit(Result.Loading)

        // Fetch Data From Local DB
        val postEntity = postDao.getPostById(postId).firstOrNull()
        if (postEntity != null) {
            val post = postEntity.toDomainModel()
            emit(Result.Success(post))
        } else {
            // If Can't Fetch Data, Then Fetch From Server
            //TODO 리팩터링 필요
            // 1. Local DB에 데이터가 없으면 서버에서 가져와서 바로 보여줌 (SSOT X)
            // 2. 대부분 페이지네이션 된 글을 읽을 것으로 예상 -> 밑에 코드 실행 확률 극히 적음
            when (val result = postNetworkDataSource.getPostById(postId)) {
                is Result.Success -> {
                    val post = result.data.toEntity().toDomainModel()
                    emit(Result.Success(post))
                }
                is Result.Error -> { emit(Result.Error(result.error)) }
                Result.Loading -> { }
            }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()
        when (val result = postNetworkDataSource.addPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun editPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val newPostNetwork = newPost.toNetwork()
        when (val result = postNetworkDataSource.editPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getPostsByTypePagination(
        type: PathParameterType,
        typeId: String,
        cursor: String?,
        limit: Int
    ): Flow<Result<PostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getPostsByType(type, typeId, cursor, limit)) {
            is Result.Success -> {
                val postList = result.data.toDomainModel()
                emit(Result.Success(postList))
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun deletePost(postId: Long): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = postNetworkDataSource.deletePost(postId)) {
            is Result.Success -> { emit(Result.Success(result.data)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getPostsByPopularUserPagination(
        categoryId: Int,
        cursor: String?,
        limit: Int,
    ): Flow<Result<PostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            postNetworkDataSource.getPostsByPopularUser(categoryId, cursor, limit)) {
            is Result.Success -> {
                val postList = result.data.toDomainModel()
                emit(Result.Success(postList))
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun likePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val likeResult = LikeRequest(profileId, postId)
        when (val result = postNetworkDataSource.likePost(likeResult)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }

    override fun unLikePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val likeResult = LikeRequest(profileId, postId)
        when (val result = postNetworkDataSource.unLikePost(likeResult)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
}