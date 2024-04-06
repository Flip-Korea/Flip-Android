package com.team.data.testdoubles.network.source

import com.team.data.network.model.request.NetworkRegister
import com.team.data.network.model.response.AccountResponse
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.retrofit.api.AuthNetworkApi
import com.team.data.network.source.AuthNetworkDataSource
import com.team.domain.util.Result
import com.team.data.testdoubles.network.model.networkRegisterTestData
import com.team.domain.util.ErrorType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthNetworkDataSource(
    private val authNetworkApi: AuthNetworkApi
): AuthNetworkDataSource {

    override suspend fun getUserAccount(accessToken: String): Flow<Result<AccountResponse, ErrorType>> =
        flow {
            val result = authNetworkApi.getUserAccount(accessToken)
            emit(Result.Success(result.body()!!))
        }

    override suspend fun checkDuplicateName(nickname: String): Flow<Result<Boolean, ErrorType>> =
        flow {
            val result = authNetworkApi.checkDuplicateName(nickname)
            emit(Result.Success(true))
        }

    override suspend fun checkDuplicateAccountId(accountId: String): Flow<Result<Boolean, ErrorType>> =
        flow {
            val result = authNetworkApi.checkDuplicateAccountId(accountId)
            emit(Result.Success(true))
        }

    override suspend fun login(accountId: String): Flow<Result<TokenResponse, ErrorType>> =
        flow {
            val result = authNetworkApi.login(accountId)
            emit(Result.Success(result.body()!!))
        }

    override suspend fun register(networkRegister: NetworkRegister): Flow<Result<TokenResponse, ErrorType>> =
        flow {
            val result = authNetworkApi.register(networkRegisterTestData)
            emit(Result.Success(result.body()!!))
        }

    override suspend fun tokenRefresh(refreshToken: String): Flow<Result<TokenResponse, ErrorType>> =
        flow {
            val result = authNetworkApi.tokenRefresh(refreshToken)
            emit(Result.Success(result.body()!!))
        }
}