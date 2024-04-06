package com.team.data.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.ProfileResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserNetworkDataSource {

    suspend fun getProfile(profileId: String): Flow<Result<ProfileResponse, ErrorType>>

    suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest
    ): Flow<Result<Boolean, ErrorType>>

    suspend fun updateMyCategory(
        profileId: String,
        category: CategoryRequest,
        accessToken: String
    ): Flow<Result<Boolean, ErrorType>>
}