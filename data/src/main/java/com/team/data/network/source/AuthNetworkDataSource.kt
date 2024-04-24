package com.team.data.network.source

import com.team.data.network.model.request.NetworkRegister
import com.team.data.network.model.response.AccountResponse
import com.team.data.network.model.response.TokenResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

import kotlinx.coroutines.flow.Flow

interface AuthNetworkDataSource {

    suspend fun getUserAccount(accessToken: String): Flow<Result<AccountResponse, ErrorType>>

    suspend fun checkDuplicateName(nickname: String): Flow<Result<Boolean, ErrorType>>

    suspend fun checkDuplicateAccountId(accountId: String): Flow<Result<Boolean, ErrorType>>

    suspend fun login(accountId: String): Flow<Result<TokenResponse, ErrorType>>

    suspend fun register(networkRegister: NetworkRegister): Flow<Result<TokenResponse, ErrorType>>

    suspend fun tokenRefresh(refreshToken: String): Flow<Result<TokenResponse, ErrorType>>

}