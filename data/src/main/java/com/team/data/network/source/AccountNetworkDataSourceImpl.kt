package com.team.data.network.source

import com.team.data.network.model.request.RegisterRequest
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.networkCall
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class AccountNetworkDataSourceImpl(
    private val authNetworkApi: AccountNetworkApi,
) : AccountNetworkDataSource {

    override suspend fun getUserAccount(accessToken: String): Result<AccountResponse, ErrorType> =
        networkCall {
            authNetworkApi.getUserAccount(accessToken)
        }

    override suspend fun checkDuplicateName(nickname: String): Result<Boolean, ErrorType> =
        networkCallWithoutResponse {
            authNetworkApi.checkDuplicateName(nickname)
        }

    override suspend fun checkDuplicateProfileId(profileId: String): Result<Boolean, ErrorType> =
        networkCallWithoutResponse {
            authNetworkApi.checkDuplicateProfileId(profileId)
        }

    override suspend fun login(accountId: String): Result<TokenResponse, ErrorType> =
        networkCall {
            authNetworkApi.login(accountId)
        }

    override suspend fun register(networkRegister: RegisterRequest): Result<TokenResponse, ErrorType> =
        networkCall {
            authNetworkApi.register(networkRegister)
        }

    override suspend fun tokenRefresh(refreshToken: String): Result<TokenResponse, ErrorType> =
        networkCall{
            authNetworkApi.tokenRefresh(refreshToken)
        }
}