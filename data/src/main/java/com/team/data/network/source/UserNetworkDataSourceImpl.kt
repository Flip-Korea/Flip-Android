package com.team.data.network.source

import com.team.data.network.model.request.BlockRequest
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.EditProfileRequest
import com.team.data.network.model.request.FollowRequest
import com.team.data.network.model.request.ReportRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.block.BlockListResponse
import com.team.data.network.model.response.category.CategoryResponseWrapper
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.MyProfileResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.data.network.networkCall
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class UserNetworkDataSourceImpl(
    private val userNetworkApi: UserNetworkApi,
) : UserNetworkDataSource {

    override suspend fun getProfile(profileId: String): Result<ProfileResponse, ErrorType> =
        networkCall {
            userNetworkApi.getProfile(profileId)
        }

    override suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Result<Boolean, ErrorType> = networkCallWithoutResponse {
        userNetworkApi.selectMyCategory(profileId, category)
    }

    override suspend fun updateMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Result<Boolean, ErrorType> = networkCallWithoutResponse {
        userNetworkApi.updateMyCategory(profileId, category)
    }

    override suspend fun getScrapList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<PostListResponse, ErrorType> = networkCall {
        userNetworkApi.getScrapList(profileId, cursor, limit)
    }

    override suspend fun editScrapComment(
        profileId: String,
        scrapId: Long,
        scrapCommentRequest: ScrapCommentRequest,
    ): Result<Boolean, ErrorType> = networkCallWithoutResponse {
        userNetworkApi.editScrapComment(profileId, scrapId, scrapCommentRequest)
    }

    override suspend fun addScrap(scrapRequest: ScrapRequest): Result<ResultIdResponse, ErrorType> =
        networkCall { userNetworkApi.addScrap(scrapRequest) }

    override suspend fun deleteScrap(scrapId: Long): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { userNetworkApi.deleteScrap(scrapId) }

    override suspend fun reportAccount(reportRequest: ReportRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { userNetworkApi.reportAccount(reportRequest)}

    override suspend fun blockAccount(blockRequest: BlockRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { userNetworkApi.blockAccount(blockRequest) }

    override suspend fun unblockAccount(
        profileId: String,
        blockedId: String,
    ): Result<Boolean, ErrorType> = networkCallWithoutResponse {
        userNetworkApi.unblockAccount(profileId, blockedId)
    }

    override suspend fun editMyProfile(
        profileId: String,
        editProfileRequest: EditProfileRequest
    ): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { userNetworkApi.editMyProfile(profileId, editProfileRequest) }

    override suspend fun follow(followRequest: FollowRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { userNetworkApi.follow(followRequest) }

    override suspend fun unfollow(followRequest: FollowRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { userNetworkApi.unfollow(followRequest) }

    override suspend fun getFollowerList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<FollowerListResponse, ErrorType> =
        networkCall { userNetworkApi.getFollowerList(profileId, cursor, limit)}

    override suspend fun getFollowingList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<FollowingListResponse, ErrorType> =
        networkCall { userNetworkApi.getFollowingList(profileId, cursor, limit) }

    override suspend fun getBlockList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<BlockListResponse, ErrorType> =
        networkCall { userNetworkApi.getBlockList(profileId, cursor, limit) }

    override suspend fun getMyCommentList(
        profileId: String,
        cursor: String,
        limit: Int,
    ): Result<MyCommentListResponse, ErrorType> =
        networkCall { userNetworkApi.getMyCommentList(profileId, cursor, limit) }

    override suspend fun getMyCategories(profileId: String): Result<CategoryResponseWrapper, ErrorType> =
        networkCall { userNetworkApi.getMyCategories(profileId) }

    override suspend fun getMyProfile(profileId: String): Result<MyProfileResponse, ErrorType> =
        networkCall { userNetworkApi.getMyProfile(profileId) }
}