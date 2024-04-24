package com.team.data.testdoubles.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.ProfileResponse
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.UserNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserNetworkDataSource(
    private val userNetworkApi: UserNetworkApi
): UserNetworkDataSource {

    override suspend fun getProfile(profileId: String): Flow<Result<ProfileResponse, ErrorType>> =
//        networkCall { userNetworkApi.getProfile(profileId) }
        flow {
//            emit(NetworkResult.Loading) // -> 테스트 시 오류 발생 (코루틴이 제대로 수집 되지 않아서 인듯)
            val result = userNetworkApi.getProfile(profileId)
            emit(Result.Success(result.body()!!))
        }

    override suspend fun selectMyCategory(
        profileId: String,
        category: CategoryRequest,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        val result = userNetworkApi.selectMyCategory(profileId, category)
        emit(Result.Success(true))
    }

    override suspend fun updateMyCategory(
        profileId: String,
        category: CategoryRequest,
        accessToken: String,
    ): Flow<Result<Boolean, ErrorType>> = flow {
        val result = userNetworkApi.updateMyCategory(profileId, category, accessToken)
        emit(Result.Success(true))
    }
}