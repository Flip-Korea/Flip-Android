package com.team.domain.usecase.profile

import com.team.domain.DataStoreManager
import com.team.domain.model.profile.MyProfile
import com.team.domain.repository.UserRepository
import com.team.domain.type.DataStoreType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository,
) {

    /**
     * Local DB 에서 나의 프로필을 가져온다.
     *
     * 만약 비어 있다면 네트워크에서 가져온다.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Result<MyProfile?, ErrorType>> {
        val profileId = dataStoreManager.getStringData(
            DataStoreType.AccountType.CURRENT_PROFILE_ID
        ).first()

        if (profileId == null) {
            return flowOf(Result.Error(ErrorType.Auth.USER_NOT_FOUND))
        }

        return userRepository.getMyProfileFromLocal(profileId).flatMapLatest { result ->
            when (result) {
                Result.Loading -> {
                    flowOf(Result.Loading)
                }

                is Result.Error -> {
                    flowOf(result)
                }

                is Result.Success -> {
                    if (result.data == null) {
                        flow {
                            userRepository.refreshMyProfile(profileId)
                            emitAll(userRepository.getMyProfileFromLocal(profileId))
                        }
                            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
                    } else {
                        flowOf(result)
                    }
                }
            }
        }
            .catch { emit(Result.Error(ErrorType.Exception.EXCEPTION)) }
    }
}