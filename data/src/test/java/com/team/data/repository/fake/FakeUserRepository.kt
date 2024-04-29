package com.team.data.repository.fake

import com.team.data.local.dao.MyProfileDao
import com.team.data.local.entity.profile.MyProfileEntity
import com.team.data.local.entity.profile.toExternal
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.FollowRequest
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.block.toExternal
import com.team.data.network.model.response.comment.toExternal
import com.team.data.network.model.response.profile.toEntity
import com.team.data.network.model.response.profile.toExternal
import com.team.data.network.source.UserNetworkDataSource
import com.team.domain.model.post.DisplayPost
import com.team.domain.model.profile.BlockProfile
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.model.profile.EditProfile
import com.team.domain.model.profile.MyProfile
import com.team.domain.model.profile.Profile
import com.team.domain.model.report_block.BlockReq
import com.team.domain.model.report_block.ReportReq
import com.team.domain.repository.UserRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FakeUserRepository(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val myProfileDao: MyProfileDao,
): UserRepository {

    private val ioDispatcher = Dispatchers.IO
    suspend fun getMyProfileRefresh(profileId: String) {
        when (val result = userNetworkDataSource.getMyProfile(profileId)) {
            is Result.Success -> {
//                val profileEntity = withContext(ioDispatcher) {
//                    result.data.toEntity()
//                }
                val profileEntity = result.data.toEntity()
                myProfileDao.upsertProfile(profileEntity)
            }
            is Result.Error -> {  }
            Result.Loading -> { }
        }
    }

    override fun getMyProfile(profileId: String): Flow<Result<MyProfile?, ErrorType>> {
        return myProfileDao.getProfileById(profileId)
            .distinctUntilChanged()
            .map<MyProfileEntity?, Result<MyProfile?, ErrorType>> { Result.Success(it?.toExternal()) }
            .catch {
                // 만약 조회하려는 데이터가 없어도 NullPointerException 발생 X
                // 위 map 블록에서 그냥 null 처리됨 -> Result.Success(null) 반환
                // 결국 밑에 if문 실행 X (혹여나 일단 작성해둠)
                if (it is NullPointerException) emit(Result.Error(ErrorType.Local.EMPTY))
                else emit(Result.Error(ErrorType.Exception.EXCEPTION))
            }

//        return flow {
//            emit(Result.Loading)
//
//            // 종단 연산자 사용 시 값 업데이트 방출 X, 중간 연산자는 가능
//            val myProfileEntity = myProfileDao.getProfileById(profileId).firstOrNull()
//            if (myProfileEntity != null) {
//                val myProfile = withContext(ioDispatcher) { myProfileEntity.toExternal() }
//                emit(Result.Success(myProfile))
//            } else { // When user delete data
//                when (val result = userNetworkDataSource.getMyProfile(profileId)) {
//                    is Result.Success -> {
//                        val profileEntity = withContext(ioDispatcher) { result.data.toEntity() }
//                        myProfileDao.upsertProfile(profileEntity)
//                        val myProfile = withContext(ioDispatcher) { profileEntity.toExternal() }
//                        emit(Result.Success(myProfile))
//                    }
//                    is Result.Error -> { emit(Result.Error(result.error)) }
//                    Result.Loading -> { }
//                }
//            }
//        }
    }

    override fun getProfile(profileId: String): Flow<Result<Profile, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.getProfile(profileId)) {
            is Result.Success -> {
//                val profile = withContext(ioDispatcher) { result.data.toExternal() }
                val profile = result.data.toExternal()
                emit(Result.Success(profile))
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> {  }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun updateMyCategory(
        profileId: String,
        categories: List<Int>,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val categoryRequest = CategoryRequest(categories)
        when (val result =
            userNetworkDataSource.updateMyCategory(profileId, categoryRequest)) {
            is Result.Success -> {
//                val myProfileEntity = withContext(ioDispatcher) {
//                    myProfileDao.updateCategories(profileId, categories)
//                    myProfileDao.getProfileById(profileId).firstOrNull()
//                }
                myProfileDao.updateCategories(profileId, categories)
                val myProfileEntity = myProfileDao.getProfileById(profileId).firstOrNull()
                emit(Result.Success(myProfileEntity != null))
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> {}
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun reportAccount(reportReq: ReportReq): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.reportAccount(reportReq.toNetwork())) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun blockAccount(blockReq: BlockReq): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.blockAccount(blockReq.toNetwork())) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun unblockAccount(
        profileId: String,
        blockedId: String,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.unblockAccount(profileId, blockedId)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun editMyProfile(profileId: String, editProfile: EditProfile): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.editMyProfile(profileId, editProfile.toNetwork())) {
            is Result.Success -> {
                val profileEntity = myProfileDao.getProfileById(profileId).firstOrNull()
                profileEntity?.let {
                    val updateProfileEntity = it
                        .copy(
                            nickname = editProfile.nickname ?: profileEntity.nickname,
                            introduce = editProfile.introduce ?: profileEntity.introduce,
                            photoUrl = editProfile.photoUrl ?: profileEntity.photoUrl
                        )
                    myProfileDao.upsertProfile(updateProfileEntity)
                } ?: emit(Result.Error(ErrorType.Local.EMPTY))
                emit(Result.Success(true))
            }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> {}
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun follow(followingId: String, followerId: String): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val followRequest = FollowRequest(followingId, followerId)
        when (val result = userNetworkDataSource.follow(followRequest)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun unfollow(
        followingId: String,
        followerId: String,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val followRequest = FollowRequest(followingId, followerId)
        when (val result = userNetworkDataSource.unfollow(followRequest)) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> { emit(Result.Error(result.error)) }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getFollowerListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<DisplayProfile>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getFollowerList(profileId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
//                    val followers = withContext(ioDispatcher) {
//                        result.data.followers.toExternal()
//                    }
                    val followers = result.data.followers.toExternal()
                    emit(Result.Success(followers))
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

    override fun getFollowingListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<DisplayProfile>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getFollowingList(profileId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
//                    val followings = withContext(ioDispatcher) {
//                        result.data.followings.toExternal()
//                    }
                    val followings = result.data.followings.toExternal()
                    emit(Result.Success(followings))
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

    override fun getBlockListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<BlockProfile>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getBlockList(profileId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
//                    val blockList = withContext(ioDispatcher) {
//                        result.data.blockList.toExternal()
//                    }
                    val blockList = result.data.blockList.toExternal()
                    emit(Result.Success(blockList))
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

    override fun getMyCommentListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<List<DisplayPost>, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getMyCommentList(profileId, cursor, limit)) {
            is Result.Success -> {
                if (result.data.hasNext && result.data.nextCursor.isNotEmpty()) {
//                    val displayPosts = withContext(ioDispatcher) {
//                        result.data.posts.toExternal()
//                    }
                    val displayPosts = result.data.posts.toExternal()
                    emit(Result.Success(displayPosts))
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