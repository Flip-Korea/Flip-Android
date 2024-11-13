package com.team.domain.usecase.interestcategory

import com.team.domain.DataStoreManager
import com.team.domain.repository.UserRepository
import com.team.domain.type.DataStoreType
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//TODO 프로필에서 카테고리를 가져 오는게 아니라
// 나중에 '나의 관심 카테고리' 만 가져 오도록 수정 필요
class GetMyCategoriesUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository,
) {

    /**
     * Local DB 에서 나의 관심 카테고리를 가져온다.
     *
     * 만약 비어 있다면 네트워크에서 가져온다.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Int>?> = flow {
        val profileId = dataStoreManager.getStringData(
            DataStoreType.AccountType.CURRENT_PROFILE_ID
        ).first()

        if (profileId == null) {
            emit(null)
            return@flow
        }

        emitAll(
            userRepository.getMyProfileFromLocal(profileId).flatMapLatest { result ->
                when (result) {
                    Result.Loading -> {
                        flowOf(emptyList())
                    }

                    is Result.Error -> {
                        flowOf(null)
                    }

                    is Result.Success -> {
                        if (result.data == null) {
                            flow {
                                userRepository.refreshMyProfile(profileId)
                                emitAll(
                                    userRepository.getMyProfileFromLocal(profileId)
                                        .filter { it is Result.Success }
                                        .map { (it as Result.Success).data?.categories }
                                )
                            }
                                .catch { emit(null) }
                        } else {
                            flowOf(result.data.categories)
                        }
                    }
                }
            }
                .catch { emit(null) }
        )
    }
}