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
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.MyProfileResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface UserNetworkDataSource {

    suspend fun getProfile(profileId: String): Result<ProfileResponse, ErrorType>

    suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest
    ): Result<Boolean, ErrorType>

    suspend fun getScrapList(
        profileId: String,
        cursor: String,
        limit: Int
    ): Result<PostListResponse, ErrorType>

    suspend fun editScrapComment(
        profileId: String,
        scrapId: Long,
        scrapCommentRequest: ScrapCommentRequest
    ): Result<Boolean, ErrorType>

    suspend fun addScrap(scrapRequest: ScrapRequest): Result<ResultIdResponse, ErrorType>

    suspend fun deleteScrap(scrapId: Long): Result<Boolean, ErrorType>

    suspend fun reportAccount(reportRequest: ReportRequest): Result<Boolean, ErrorType>

    suspend fun blockAccount(blockRequest: BlockRequest): Result<Boolean, ErrorType>

    suspend fun unblockAccount(
        profileId: String,
        blockedId: String
    ): Result<Boolean, ErrorType>

    suspend fun editMyProfile(
        profileId: String,
        editProfileRequest: EditProfileRequest
    ): Result<Boolean, ErrorType>

    suspend fun follow(followRequest: FollowRequest): Result<Boolean, ErrorType>

    suspend fun unfollow(followRequest: FollowRequest): Result<Boolean, ErrorType>

    suspend fun getFollowerList(
        profileId: String,
        cursor: String,
        limit: Int
    ): Result<FollowerListResponse, ErrorType>

    suspend fun getFollowingList(
        profileId: String,
        cursor: String,
        limit: Int
    ): Result<FollowingListResponse, ErrorType>

    suspend fun getBlockList(
        profileId: String,
        cursor: String,
        limit: Int
    ): Result<BlockListResponse, ErrorType>

    suspend fun getMyCommentList(
        profileId: String,
        cursor: String,
        limit: Int
    ): Result<MyCommentListResponse, ErrorType>

    suspend fun getMyCategories(profileId: String): Result<List<CategoryResponse>, ErrorType>

    suspend fun getMyProfile(profileId: String): Result<MyProfileResponse, ErrorType>
}