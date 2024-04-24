package com.team.data.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.post.PostListResponse
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
}