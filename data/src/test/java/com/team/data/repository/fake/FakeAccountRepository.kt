package com.team.data.repository.fake

import com.team.data.datastore.fake.FakeDataStoreManager
import com.team.data.local.dao.MyProfileDao
import com.team.data.local.entity.profile.toDomainModel
import com.team.data.network.model.request.toNetwork
import com.team.data.network.model.response.account.toDomainModel
import com.team.data.network.model.response.profile.toEntity
import com.team.data.network.source.AccountNetworkDataSource
import com.team.domain.model.account.Account
import com.team.domain.model.account.Register
import com.team.domain.repository.AccountRepository
import com.team.domain.type.DataStoreType
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.type.asString
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class FakeAccountRepository(
    private val accountNetworkDataSource: AccountNetworkDataSource,
    private val myProfileDao: MyProfileDao,
    private val dataStoreManager: FakeDataStoreManager,
): AccountRepository {

    private val ioDispatcher = Dispatchers.IO

    override fun changeProfile(profileId: String): Flow<Result<Boolean, ErrorType>> = flow {
        emit(Result.Loading)

//        dataStoreManager.deleteToken(DataStoreManager.AccountType.CURRENT_PROFILE_ID)
        try {
            dataStoreManager.saveData(
                DataStoreType.AccountType.CURRENT_PROFILE_ID,
                profileId
            )
            emit(Result.Success(true))
        } catch (e: NullPointerException) {
            emit(Result.Error(ErrorType.Token.NOT_FOUND))
        } catch (e: IOException) {
            emit(Result.Error(ErrorType.Exception.IO))
        } catch (e: Exception) {
            emit(Result.Error(ErrorType.Exception.EXCEPTION))
        }
    }

    override fun getUserAccount(): Flow<Result<Account, ErrorType>> {
        return flow {
            emit(Result.Loading)

            val accessToken = dataStoreManager.getData(DataStoreType.TokenType.ACCESS_TOKEN).firstOrNull()
            accessToken?.let { aT ->
                when (val result = accountNetworkDataSource.getUserAccount(aT)) {
                    is Result.Success -> {
                        // 데이터 동기화 전략 사용 (민감데이터 제외)
                        // 민감데이터를 제외한 프로필 데이터만 기기에 저장
                        // 민감데이터 필요 시, 서버에서 가져온 데이터 그대로 사용
                        // API 명세서에 따라 현재 프로필 제외하고는 ID만 가져오게 바뀔 가능성 있음
//                        val account = withContext(ioDispatcher) {
//                            myProfileDao.refresh(result.data.profile.toEntity())
//                            val myProfileEntities = myProfileDao.getAllProfile().first()
//                            result.data.toExternal(myProfileEntities.toExternal())
//                        }

                        myProfileDao.refresh(result.data.profile.toEntity())
                        val myProfileEntities = myProfileDao.getAllProfile().first()
                        val account = result.data.toDomainModel(myProfileEntities.toDomainModel())

                        //TODO 현재 저장된 ProfileId가 없다면 저장 (해당 위치가 맞는지 확인 필요)
                        // 만약 멀티프로필 기능 추가 시 현재 프로필ID로 바꿔주는 함수 필요
                        val currentProfile = dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID)
                            .catch { emit("") }
                            .first()
                        if (currentProfile.isNullOrEmpty()) {
                            dataStoreManager.saveData(
                                DataStoreType.AccountType.CURRENT_PROFILE_ID,
                                account.profiles[0].profileId
                            )
                        }

                        emit(Result.Success(account))
                    }
                    is Result.Error -> { emit(Result.Error(result.error)) }
                    Result.Loading -> { }
                }
            } ?: emit(Result.Error(ErrorType.Token.NOT_FOUND))
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    override fun checkDuplicateName(nickname: String): Flow<Result<Boolean, ErrorType>> {
        return flow {
            emit(Result.Loading)

            when (val result = accountNetworkDataSource.checkDuplicateName(nickname)) {
                is Result.Success -> { emit(Result.Success(result.data)) }
                is Result.Error -> { emit(Result.Error(result.error)) }
                Result.Loading -> { }
            }
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    override fun checkDuplicateProfileId(profileId: String): Flow<Result<Boolean, ErrorType>> {
        return flow {
            emit(Result.Loading)

            when (val result = accountNetworkDataSource.checkDuplicateProfileId(profileId)) {
                is Result.Success -> { emit(Result.Success(result.data)) }
                is Result.Error -> { emit(Result.Error(result.error)) }
                Result.Loading -> { }
            }
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    override fun login(
        loginPlatformType: SocialLoginPlatform,
        accountId: String
    ): Flow<Result<Boolean, ErrorType>> {
        return flow {
            emit(Result.Loading)

            val accountIdResult = loginPlatformType.asString()+accountId

            when (val result = accountNetworkDataSource.login(accountIdResult)) {
                is Result.Success -> {
                    saveTokens(result.data.accessToken, result.data.refreshToken)
                    emit(Result.Success(true))
                }
                is Result.Error -> { emit(Result.Error(result.error)) }
                Result.Loading -> { }
            }
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    override fun register(register: Register): Flow<Result<Boolean, ErrorType>> {
        return flow {
            emit(Result.Loading)

            when(val result = accountNetworkDataSource.register(register.toNetwork())) {
                is Result.Success -> {
                    saveTokens(result.data.accessToken, result.data.refreshToken)
                    emit(Result.Success(true))
                }
                is Result.Error -> { emit(Result.Error(result.error)) }
                Result.Loading -> { }
            }
        }
            .flowOn(ioDispatcher)
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStoreManager.saveData(DataStoreType.TokenType.ACCESS_TOKEN, accessToken)
        dataStoreManager.saveData(DataStoreType.TokenType.REFRESH_TOKEN, refreshToken)
    }
}