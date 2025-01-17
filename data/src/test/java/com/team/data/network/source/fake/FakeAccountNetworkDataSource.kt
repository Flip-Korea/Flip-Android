package com.team.data.network.source.fake

import com.team.data.network.model.request.LoginRequest
import com.team.data.network.model.request.NicknameValidationRequest
import com.team.data.network.model.request.ProfileIdValidationRequest
import com.team.data.network.model.request.RegisterRequest
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.data.network.source.AccountNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakeAccountNetworkDataSource(
    private val accountNetworkApi: AccountNetworkApi,
) : AccountNetworkDataSource {
    override suspend fun getUserAccount(accessToken: String): Result<AccountResponse, ErrorType> {
        val result = accountNetworkApi.getUserAccount(accessToken)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun validateNickname(
        nicknameValidationRequest: NicknameValidationRequest,
    ): Result<Boolean, ErrorType> {
        val result = accountNetworkApi.validateNickname(nicknameValidationRequest)
        return if (result.isSuccessful) {
            when (result.code()) {
                200 -> {
                    Result.Success(true)
                }

                else -> {
                    Result.Success(true)
                }
            }
        } else {
            when (result.code()) {
                400 -> {
                    Result.Error(ErrorType.Network.BAD_REQUEST)
                }

                404 -> {
                    Result.Error(ErrorType.Network.NOT_FOUND)
                }

                else -> {
                    Result.Error(ErrorType.Network.UNEXPECTED)
                }
            }
        }
    }

    override suspend fun validateProfileId(
        profileIdValidationRequest: ProfileIdValidationRequest,
    ): Result<Boolean, ErrorType> {
        val result = accountNetworkApi.validateProfileId(profileIdValidationRequest)
        return if (result.isSuccessful) {
            when (result.code()) {
                200 -> {
                    Result.Success(true)
                }

                else -> {
                    Result.Success(true)
                }
            }
        } else {
            when (result.code()) {
                400 -> {
                    Result.Error(ErrorType.Network.BAD_REQUEST)
                }

                404 -> {
                    Result.Error(ErrorType.Network.NOT_FOUND)
                }

                else -> {
                    Result.Error(ErrorType.Network.UNEXPECTED)
                }
            }
        }
    }

    override suspend fun login(loginRequest: LoginRequest): Result<TokenResponse, ErrorType> {
        val result = accountNetworkApi.login(loginRequest)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun register(
        networkRegister: RegisterRequest,
    ): Result<TokenResponse, ErrorType> {
        val result = accountNetworkApi.register(networkRegister)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun tokenRefresh(refreshToken: String): Result<TokenResponse, ErrorType> {
        val result = accountNetworkApi.tokenRefresh(refreshToken)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}
