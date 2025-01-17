package com.team.data.network.source

import com.team.data.network.model.request.NicknameValidationRequest
import com.team.data.network.model.request.ProfileIdValidationRequest
import com.team.data.network.model.request.RegisterRequest
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface AccountNetworkDataSource {
    suspend fun getUserAccount(accessToken: String): Result<AccountResponse, ErrorType>

    suspend fun validateNickname(
        nicknameValidationRequest: NicknameValidationRequest,
    ): Result<Boolean, ErrorType>

    suspend fun validateProfileId(
        profileIdValidationRequest: ProfileIdValidationRequest,
    ): Result<Boolean, ErrorType>

    suspend fun login(accountId: String): Result<TokenResponse, ErrorType>

    suspend fun register(networkRegister: RegisterRequest): Result<TokenResponse, ErrorType>

    suspend fun tokenRefresh(refreshToken: String): Result<TokenResponse, ErrorType>
}
