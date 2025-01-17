package com.team.data.repository

import com.team.data.di.IODispatcher
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.request.toNicknameValidationRequest
import com.team.data.network.model.request.toProfileIdValidationRequest
import com.team.data.network.source.AccountNetworkDataSource
import com.team.domain.DataStoreManager
import com.team.domain.model.account.Login
import com.team.domain.model.account.NicknameValidation
import com.team.domain.model.account.ProfileIdValidation
import com.team.domain.model.account.Register
import com.team.domain.repository.AccountRepository
import com.team.domain.type.DataStoreType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultAccountRepository @Inject constructor(
    private val accountNetworkDataSource: AccountNetworkDataSource,
    private val dataStoreManager: DataStoreManager,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : AccountRepository {
    override fun validateNickname(
        nicknameValidation: NicknameValidation,
    ): Flow<Result<Boolean, ErrorType>> =
        flow {
            emit(Result.Loading)

            val nicknameValidationRequest = nicknameValidation.toNicknameValidationRequest()
            when (
                val result = accountNetworkDataSource.validateNickname(nicknameValidationRequest)
            ) {
                is Result.Success -> {
                    emit(Result.Success(result.data))
                }

                is Result.Error -> {
                    emit(Result.Error(errorBody = result.errorBody, error = result.error))
                }

                Result.Loading -> {}
            }
        }.flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun validateProfileId(
        profileIdValidation: ProfileIdValidation,
    ): Flow<Result<Boolean, ErrorType>> =
        flow {
            emit(Result.Loading)

            val profileIdValidationRequest = profileIdValidation.toProfileIdValidationRequest()
            when (
                val result =
                    accountNetworkDataSource.validateProfileId(profileIdValidationRequest)
            ) {
                is Result.Success -> {
                    emit(Result.Success(result.data))
                }

                is Result.Error -> {
                    emit(Result.Error(errorBody = result.errorBody, error = result.error))
                }

                Result.Loading -> {}
            }
        }.flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun login(login: Login): Flow<Result<Boolean, ErrorType>> =
        flow {
            emit(Result.Loading)

            when (val result = accountNetworkDataSource.login(login.toNetwork())) {
                is Result.Success -> {
                    saveTokens(result.data.accessToken, result.data.refreshToken)
                    emit(Result.Success(true))
                }

                is Result.Error -> {
                    emit(Result.Error(errorBody = result.errorBody, error = result.error))
                }

                Result.Loading -> {}
            }
        }.flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    override fun register(register: Register): Flow<Result<Boolean, ErrorType>> =
        flow {
            emit(Result.Loading)

            when (val result = accountNetworkDataSource.register(register.toNetwork())) {
                is Result.Success -> {
                    saveTokens(result.data.accessToken, result.data.refreshToken)
                    emit(Result.Success(true))
                }

                is Result.Error -> {
                    emit(Result.Error(errorBody = result.errorBody, error = result.error))
                }

                Result.Loading -> {}
            }
        }.flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }

    private suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        dataStoreManager.saveData(DataStoreType.TokenType.ACCESS_TOKEN, accessToken)
        dataStoreManager.saveData(DataStoreType.TokenType.REFRESH_TOKEN, refreshToken)
    }
}
