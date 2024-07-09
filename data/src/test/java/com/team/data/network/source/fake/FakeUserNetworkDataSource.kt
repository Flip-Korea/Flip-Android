package com.team.data.network.source.fake

import com.team.data.network.model.request.BlockRequest
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.EditProfileRequest
import com.team.data.network.model.request.FollowRequest
import com.team.data.network.model.request.ReportRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.block.BlockListResponse
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.MyProfileResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.UserNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakeUserNetworkDataSource(
    private val userNetworkApi: UserNetworkApi,
) : UserNetworkDataSource {

    override suspend fun getMyProfile(profileId: String): Result<MyProfileResponse, ErrorType> {
        val result = userNetworkApi.getMyProfile(profileId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getProfile(profileId: String): Result<ProfileResponse, ErrorType> {
//            emit(NetworkResult.Loading) // -> 테스트 시 오류 발생 (코루틴이 제대로 수집 되지 않아서 인듯)
        val result = userNetworkApi.getProfile(profileId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.selectMyCategory(profileId, category)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getScrapList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> {
        val result = userNetworkApi.getScrapList(profileId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun editScrapComment(
        profileId: String,
        scrapId: Long,
        scrapCommentRequest: ScrapCommentRequest,
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.editScrapComment(profileId, scrapId, scrapCommentRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun addScrap(scrapRequest: ScrapRequest): Result<ResultIdResponse, ErrorType> {
        val result = userNetworkApi.addScrap(scrapRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun deleteScrap(scrapId: Long): Result<Boolean, ErrorType> {
        val result = userNetworkApi.deleteScrap(scrapId)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun reportAccount(reportRequest: ReportRequest): Result<Boolean, ErrorType> {
        val result = userNetworkApi.reportAccount(reportRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun blockAccount(blockRequest: BlockRequest): Result<Boolean, ErrorType> {
        val result = userNetworkApi.blockAccount(blockRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun unblockAccount(
        profileId: String,
        blockedId: String,
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.unblockAccount(profileId, blockedId)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun editMyProfile(
        profileId: String,
        editProfileRequest: EditProfileRequest
    ): Result<Boolean, ErrorType> {
        val result = userNetworkApi.editMyProfile(profileId, editProfileRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun follow(followRequest: FollowRequest): Result<Boolean, ErrorType> {
        val result = userNetworkApi.follow(followRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun unfollow(followRequest: FollowRequest): Result<Boolean, ErrorType> {
        val result = userNetworkApi.unfollow(followRequest)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getFollowerList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<FollowerListResponse, ErrorType> {
        val result = userNetworkApi.getFollowerList(profileId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getFollowingList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<FollowingListResponse, ErrorType> {
        val result = userNetworkApi.getFollowingList(profileId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getBlockList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<BlockListResponse, ErrorType> {
        val result = userNetworkApi.getBlockList(profileId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getMyCommentList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<MyCommentListResponse, ErrorType> {
        val result = userNetworkApi.getMyCommentList(profileId, cursor, limit)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun getMyCategories(profileId: String): Result<List<CategoryResponse>, ErrorType> {
        val result = userNetworkApi.getMyCategories(profileId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}