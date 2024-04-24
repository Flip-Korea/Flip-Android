package com.team.data.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.ProfileResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface UserNetworkDataSource {

    suspend fun getProfile(profileId: String): Result<ProfileResponse, ErrorType>

    suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest
    ): Result<Boolean, ErrorType>

    suspend fun updateMyCategory(
        profileId: String,
        category: CategoryRequest,
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
}