package com.team.data.network.source

import com.team.data.network.model.request.NetworkRegister
import com.team.data.network.model.response.AccountResponse
import com.team.data.network.networkCallFlow
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.AuthNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

class AuthNetworkDataSourceImpl(
    private val authNetworkApi: AuthNetworkApi,
) : AuthNetworkDataSource {

    override suspend fun getUserAccount(accessToken: String): Flow<Result<AccountResponse, ErrorType>> =
        networkCallFlow {
            authNetworkApi.getUserAccount(accessToken)
        }

    override suspend fun checkDuplicateName(nickname: String): Flow<Result<Boolean, ErrorType>> =
        networkCallWithoutResponse {
            authNetworkApi.checkDuplicateName(nickname)
        }

    override suspend fun checkDuplicateAccountId(accountId: String): Flow<Result<Boolean, ErrorType>> =
        networkCallWithoutResponse {
            authNetworkApi.checkDuplicateAccountId(accountId)
        }

    override suspend fun login(accountId: String): Flow<Result<TokenResponse, ErrorType>> =
        networkCallFlow {
            authNetworkApi.login(accountId)
        }

    override suspend fun register(networkRegister: NetworkRegister): Flow<Result<TokenResponse, ErrorType>> =
        networkCallFlow {
            authNetworkApi.register(networkRegister)
        }

    override suspend fun tokenRefresh(refreshToken: String): Flow<Result<TokenResponse, ErrorType>> =
        networkCallFlow {
            authNetworkApi.tokenRefresh(refreshToken)
        }
}