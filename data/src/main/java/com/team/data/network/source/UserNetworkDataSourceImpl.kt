package com.team.data.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.ProfileResponse
import com.team.data.network.networkCallFlow
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

class UserNetworkDataSourceImpl(
    private val userNetworkApi: UserNetworkApi,
) : UserNetworkDataSource {

    override suspend fun getProfile(profileId: String): Flow<Result<ProfileResponse, ErrorType>> =
        networkCallFlow {
            userNetworkApi.getProfile(profileId)
        }

    override suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Flow<Result<Boolean, ErrorType>> = networkCallWithoutResponse {
        userNetworkApi.selectMyCategory(profileId, category)
    }

    override suspend fun updateMyCategory(
        profileId: String,
        category: CategoryRequest,
        accessToken: String,
    ): Flow<Result<Boolean, ErrorType>> = networkCallWithoutResponse {
        userNetworkApi.updateMyCategory(profileId, category, accessToken)
    }
}