package com.team.domain.repository

import com.team.domain.model.post.DisplayPostList
import com.team.domain.model.profile.BlockProfileList
import com.team.domain.model.profile.DisplayProfileList
import com.team.domain.model.profile.EditProfile
import com.team.domain.model.profile.MyProfile
import com.team.domain.model.profile.Profile
import com.team.domain.model.report_block.BlockReq
import com.team.domain.model.report_block.ReportReq
import com.team.domain.usecase.category.myCategoriesTestData
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class FakeUserRepository(
    private val hasLocalData: Boolean,
    private val isNetworkError: Boolean,
) : UserRepository {

    private val profile: MutableStateFlow<MyProfile?> = MutableStateFlow(null)

    init {
        if (hasLocalData) {
            profile.update {
                MyProfile(
                    "profileId",
                    "",
                    "",
                    "",
                    0,
                    0,
                    0,
                    myCategoriesTestData.map { c -> c.id },
                    ""
                )
            }
        }
    }

    override fun getMyProfileFromLocal(profileId: String): Flow<Result<MyProfile?, ErrorType>> =
        flow {
            emit(Result.Success(profile.value))
        }

    override suspend fun refreshMyProfile(profileId: String) {
        if (isNetworkError) {
            profile.update { null }
        } else {
            profile.update {
                MyProfile(
                    profileId,
                    "",
                    "",
                    "",
                    0,
                    0,
                    0,
                    myCategoriesTestData.map { c -> c.id },
                    ""
                )
            }
        }
    }

    override fun getProfile(profileId: String): Flow<Result<Profile, ErrorType>> {
        return emptyFlow()
    }

    override fun updateMyCategories(
        categoryIds: List<Int>,
    ): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun reportAccount(reportReq: ReportReq): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun blockAccount(blockReq: BlockReq): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun unblockAccount(
        profileId: String,
        blockedId: String,
    ): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun editMyProfile(
        profileId: String,
        editProfile: EditProfile,
    ): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun follow(followingId: String, followerId: String): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun unfollow(
        followingId: String,
        followerId: String,
    ): Flow<Result<Boolean, ErrorType>> {
        return emptyFlow()
    }

    override fun getFollowerListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayProfileList, ErrorType>> {
        return emptyFlow()
    }

    override fun getFollowingListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayProfileList, ErrorType>> {
        return emptyFlow()
    }

    override fun getBlockListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<BlockProfileList, ErrorType>> {
        return emptyFlow()
    }

    override fun getMyCommentListPagination(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Flow<Result<DisplayPostList, ErrorType>> {
        return emptyFlow()
    }
}