package com.team.data.repository.fake

import com.team.data.FlipPagination
import com.team.data.local.dao.PostDao
import com.team.data.local.entity.post.toExternal
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.post.toEntity
import com.team.data.network.model.response.post.toExternal
import com.team.data.network.source.PostNetworkDataSource
import com.team.domain.model.post.NewPost
import com.team.domain.model.post.Post
import com.team.domain.repository.PostRepository
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

//TODO 실제 Repository로 통합해서 테스트하기
class FakePostRepository(
    private val postDao: PostDao,
    private val postNetworkDataSource: PostNetworkDataSource,
): PostRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun getPosts(): Flow<List<Post>> =
        postDao.getPosts().map { it.toExternal() }

    override fun getPostsPagination(cursor: String): Flow<Result<Boolean, ErrorType>> {
        return flow {
            emit(Result.Loading)

            when (val postList = postNetworkDataSource.getPosts(cursor, FlipPagination.PAGE_SIZE)) {
                is Result.Success -> {
                    if (postList.data.hasNext && postList.data.nextCursor.isNotEmpty()) {
//                        val postListEntities = withContext(ioDispatcher) {
//                            postList.data.posts.map { it.toEntity() }
//                        }
                        val postListEntities = postList.data.posts.map { it.toEntity() }
                        postDao.refresh(postListEntities)
                    }
                    emit(Result.Success(postList.data.hasNext))
                }
                is Result.Error -> { emit(Result.Error(postList.error)) }
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
//            val post = withContext((ioDispatcher)) { postEntity.toExternal() }
            val post = postEntity.toExternal()
            emit(Result.Success(post))
        } else {
            // If Can't Fetch Data, Then Fetch From Server
            //TODO 리팩터링 필요
            // 1. Local DB에 데이터가 없으면 서버에서 가져와서 바로 보여줌 (SSOT X)
            // 2. 대부분 페이지네이션 된 글을 읽을 것으로 예상 -> 밑에 코드 실행 확률 극히 적음
            when (val postNetwork = postNetworkDataSource.getPostById(postId)) {
                is Result.Success -> {
//                    val post = withContext(ioDispatcher) {
//                        postNetwork.data.toEntity().toExternal()
//                    }
                    val post = postNetwork.data.toEntity().toExternal()
                    emit(Result.Success(post))
                }
                is Result.Error -> { emit(Result.Error(postNetwork.error)) }
                Result.Loading -> { }
            }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun addPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        // 원래는 데이터 동기화 전략을 따라야 하지만 PostId를 서버에서 생성하기 때문에
        // 먼저 서버에 저장 후 응답 받은 PostId를 포함한 PostEntity를 기기에 저장 (추후 로직)
//        val newPostNetwork = withContext(ioDispatcher) { newPost.toNetwork() }
        val newPostNetwork = newPost.toNetwork()
        when (val resultId = postNetworkDataSource.addPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(resultId.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun editPost(newPost: NewPost): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        // addPost와 마찬가지로 PostId를 서버에서 받아옴
//        val newPostNetwork = withContext(ioDispatcher) { newPost.toNetwork() }
        val newPostNetwork = newPost.toNetwork()
        when (val resultId = postNetworkDataSource.editPost(newPostNetwork)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(resultId.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getPostsByTypePagination(
        type: PathParameterType,
        typeId: String,
        cursor: String,
    ): Flow<Result<List<Post>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val postResult =
            postNetworkDataSource.getPostsByType(type, typeId, cursor, FlipPagination.PAGE_SIZE)) {
            is Result.Success -> {
                if (postResult.data.hasNext && postResult.data.nextCursor.isNotEmpty()) {
//                    val postList = withContext(ioDispatcher) {
//                        postResult.data.posts.toExternal()
//                    }
                    val postList = postResult.data.posts.toExternal()
                    emit(Result.Success(postList))
                } else {
                    emit(Result.Success(emptyList()))
                }
            }
            is Result.Error -> { emit(Result.Error(postResult.error)) }
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
        cursor: String,
        limit: Int,
    ): Flow<Result<List<Post>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val postResult =
            postNetworkDataSource.getPostsByPopularUser(categoryId, cursor, limit)) {
            is Result.Success -> {
                if (postResult.data.hasNext && postResult.data.nextCursor.isNotEmpty()) {
//                    val postList = withContext(ioDispatcher) {
//                        postResult.data.posts.toExternal()
//                    }
                    val postList = postResult.data.posts.toExternal()
                    emit(Result.Success(postList))
                } else {
                    emit(Result.Success(emptyList()))
                }
            }
            is Result.Error -> { emit(Result.Error(postResult.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun likePost(profileId: String, postId: Int): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val likeResult = LikeRequest(profileId, postId)
        when (val resultId = postNetworkDataSource.likePost(likeResult)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(resultId.error)) }
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