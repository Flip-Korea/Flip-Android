package com.team.data.repository

import com.team.data.di.IODispatcher
import com.team.data.local.dao.MyProfileDao
import com.team.data.local.entity.profile.MyProfileEntity
import com.team.data.local.entity.profile.toDomainModel
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.FollowRequest
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.block.toDomainModel
import com.team.data.network.model.response.comment.toDomainModel
import com.team.data.network.model.response.follow.toDomainModel
import com.team.data.network.model.response.profile.toDomainModel
import com.team.data.network.model.response.profile.toEntity
import com.team.data.network.source.InterestCategoryNetworkDataSource
import com.team.data.network.source.UserNetworkDataSource
import com.team.domain.DataStoreManager
import com.team.domain.model.post.DisplayPostList
import com.team.domain.model.profile.BlockProfileList
import com.team.domain.model.profile.DisplayProfileList
import com.team.domain.model.profile.EditProfile
import com.team.domain.model.profile.MyProfile
import com.team.domain.model.profile.Profile
import com.team.domain.model.report_block.BlockReq
import com.team.domain.model.report_block.ReportReq
import com.team.domain.repository.UserRepository
import com.team.domain.type.DataStoreType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO:
// updateMyCategory, editMyProfile 부분은 데이터 동기화 과정으로 작성했으나,
// 만약, Local/Network 작업 중 하나라도 에러가 발생하면 데이터 일관성이 깨짐
// 1. WorkManager를 통한 Sync 작업을 요함
// 2. 혹은 서버 우선 작업을 요함 (선택)
// (-> 서버의 성공적인 응답에 따라 Local 작업 진행)
class DefaultUserRepository @Inject constructor(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val interestCategoryNetworkDataSource: InterestCategoryNetworkDataSource,
    private val dataStoreManager: DataStoreManager,
    private val myProfileDao: MyProfileDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
): UserRepository {

    override fun getMyProfileFromLocal(profileId: String): Flow<Result<MyProfile?, ErrorType>> {
        return myProfileDao.getProfileById(profileId)
            .distinctUntilChanged()
            .map<MyProfileEntity?, Result<MyProfile?, ErrorType>> { Result.Success(it?.toDomainModel()) }
            .flowOn(ioDispatcher)
            .catch {
                /** 만약 조회하려는 데이터가 없어도 NullPointerException 발생 X
                 * 위 map 블록에서 그냥 null 처리됨 -> Result.Success(null) 반환
                 * 결국 밑에 if문 실행 X (혹여나 일단 작성해둠)
                 */
                if (it is NullPointerException) emit(Result.Success(null))
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

    override suspend fun refreshMyProfile(profileId: String) {
        val result = withContext(ioDispatcher) {
            userNetworkDataSource.getMyProfile(profileId)
        }
        when (result) {
            is Result.Success -> {
                val profileEntity = withContext(ioDispatcher) {
                    result.data.toEntity()
                }
                withContext(ioDispatcher) {
                    myProfileDao.upsertProfile(profileEntity)
                }
            }
            is Result.Error -> { }
            Result.Loading -> { }
        }
    }

    override fun getProfile(profileId: String): Flow<Result<Profile, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.getProfile(profileId)) {
            is Result.Success -> {
                val profile = result.data.toDomainModel()
                emit(Result.Success(profile))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> {  }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun updateMyCategories(
        categoryIds: List<Int>,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        val categoryRequest = CategoryRequest(categoryIds)
        when (val result =
            interestCategoryNetworkDataSource.updateMyCategories(categoryRequest)) {
            is Result.Success -> {
                val profileId = dataStoreManager.getStringData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()
                myProfileDao.updateCategories(profileId ?: "", categoryIds)
                val myProfileEntity = myProfileDao.getProfileById(profileId ?: "").firstOrNull()
                emit(Result.Success(myProfileEntity != null))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> {}
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun reportAccount(reportReq: ReportReq): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.reportAccount(reportReq.toNetwork())) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun blockAccount(blockReq: BlockReq): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result = userNetworkDataSource.blockAccount(blockReq.toNetwork())) {
            is Result.Success -> { emit(Result.Success(true)) }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
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
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
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
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
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
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
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
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getFollowerListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayProfileList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getFollowerList(profileId, cursor, limit)) {
            is Result.Success -> {
                val followers = result.data.toDomainModel()
                emit(Result.Success(followers))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getFollowingListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayProfileList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getFollowingList(profileId, cursor, limit)) {
            is Result.Success -> {
                val followings = result.data.toDomainModel()
                emit(Result.Success(followings))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getBlockListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<BlockProfileList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getBlockList(profileId, cursor, limit)) {
            is Result.Success -> {
                val blockList = result.data.toDomainModel()
                emit(Result.Success(blockList))
            }
            is Result.Error -> {
                emit(Result.Error(errorBody = result.errorBody, error = result.error))
            }
            Result.Loading -> { }
        }
    }
        .flowOn(ioDispatcher)
        .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun getMyCommentListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayPostList, ErrorType>> = flow {
        emit(Result.Loading)

        when (val result =
            userNetworkDataSource.getMyCommentList(profileId, cursor, limit)) {
            is Result.Success -> {
                val displayPosts = result.data.toDomainModel()
                emit(Result.Success(displayPosts))
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