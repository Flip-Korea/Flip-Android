package com.team.data.network.source.fake

import com.team.data.network.model.request.RegisterRequest
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.data.network.source.AccountNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakeAccountNetworkDataSource(
    private val accountNetworkApi: AccountNetworkApi
): AccountNetworkDataSource {

    override suspend fun getUserAccount(accessToken: String): Result<AccountResponse, ErrorType> {
        val result = accountNetworkApi.getUserAccount(accessToken)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun checkDuplicateName(nickname: String): Result<Boolean, ErrorType> {
        val result = accountNetworkApi.checkDuplicateName(nickname)
        return if (result.isSuccessful) {
            when (result.code()) {
                200 -> { Result.Success(true) }
                else -> { Result.Success(true) }
            }
        } else {
            when (result.code()) {
                400 -> { Result.Error(ErrorType.Network.BAD_REQUEST) }
                404 -> { Result.Error(ErrorType.Network.NOT_FOUND) }
                else -> { Result.Error(ErrorType.Network.UNEXPECTED) }
            }
        }
    }

    override suspend fun checkDuplicateProfileId(profileId: String): Result<Boolean, ErrorType> {
        val result = accountNetworkApi.checkDuplicateProfileId(profileId)
        return if (result.isSuccessful) {
            when (result.code()) {
                200 -> { Result.Success(true) }
                else -> { Result.Success(true) }
            }
        } else {
            when (result.code()) {
                400 -> { Result.Error(ErrorType.Network.BAD_REQUEST) }
                404 -> { Result.Error(ErrorType.Network.NOT_FOUND) }
                else -> { Result.Error(ErrorType.Network.UNEXPECTED) }
            }
        }
    }

    override suspend fun login(accountId: String): Result<TokenResponse, ErrorType> {
        val result = accountNetworkApi.login(accountId)
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun register(networkRegister: RegisterRequest): Result<TokenResponse, ErrorType> {
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