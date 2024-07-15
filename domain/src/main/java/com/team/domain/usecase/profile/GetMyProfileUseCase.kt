package com.team.domain.usecase.profile

import com.team.domain.DataStoreManager
import com.team.domain.model.profile.MyProfile
import com.team.domain.repository.UserRepository
import com.team.domain.type.DataStoreType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Local DB 에서 나의 프로필을 가져온다.
 *
 * 만약 비어 있다면 네트워크에서 가져온다.
 */
class GetMyProfileUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Result<MyProfile, ErrorType>> = flow {
        val profileId = dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()
        profileId?.let {

            userRepository.getMyProfileFromLocal(profileId).flatMapLatest { result ->
                when(result) {
                    is Result.Success -> {
                        if (result.data == null) {
                            userRepository.refreshMyProfile(profileId)
                            val result2 = userRepository.getMyProfileFromLocal(profileId).first()
                            flowOf(
                                when(result2) {
                                    is Result.Success -> {
                                        Result.Success(result2.data)
                                    }
                                    is Result.Error -> {
                                        Result.Error(
                                            error = result2.error,
                                            errorBody = result2.errorBody
                                        )
                                    }
                                    Result.Loading -> { Result.Loading }
                                }
                            )
                        } else {
                            flowOf(Result.Success(result.data))
                        }
                    }
                    is Result.Error -> {
                        flowOf(Result.Error(
                            error = result.error,
                            errorBody = result.errorBody
                        ))
                    }
                    Result.Loading -> { flowOf(Result.Loading) }
                }
            }

        } ?: emit(Result.Error(ErrorType.Local.EMPTY))
    }
}