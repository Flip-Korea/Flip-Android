package com.team.data.network.source

import com.team.data.network.model.request.NicknameValidationRequest
import com.team.data.network.model.request.ProfileIdValidationRequest
import com.team.data.network.model.request.RegisterRequest
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.networkCall
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class AccountNetworkDataSourceImpl(
    private val accountNetworkApi: AccountNetworkApi,
) : AccountNetworkDataSource {
    override suspend fun getUserAccount(accessToken: String): Result<AccountResponse, ErrorType> =
        networkCall {
            accountNetworkApi.getUserAccount(accessToken)
        }

    override suspend fun validateNickname(
        nicknameValidationRequest: NicknameValidationRequest,
    ): Result<Boolean, ErrorType> =
        networkCallWithoutResponse {
            accountNetworkApi.validateNickname(nicknameValidationRequest)
        }

    override suspend fun validateProfileId(
        profileIdValidationRequest: ProfileIdValidationRequest,
    ): Result<Boolean, ErrorType> =
        networkCallWithoutResponse {
            accountNetworkApi.validateProfileId(profileIdValidationRequest)
        }

    override suspend fun login(accountId: String): Result<TokenResponse, ErrorType> =
        networkCall {
            accountNetworkApi.login(accountId)
        }

    override suspend fun register(
        networkRegister: RegisterRequest,
    ): Result<TokenResponse, ErrorType> =
        networkCall {
            accountNetworkApi.register(networkRegister)
        }

    override suspend fun tokenRefresh(refreshToken: String): Result<TokenResponse, ErrorType> =
        networkCall {
            accountNetworkApi.tokenRefresh(refreshToken)
        }
}
