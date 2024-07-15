package com.team.domain.usecase.interestcategory

import com.team.domain.repository.UserRepository
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Local DB 에서 나의 관심 카테고리를 가져온다.
 *
 * 만약 비어 있다면 네트워크에서 가져온다.
 */
class GetMyCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(profileId: String): Flow<List<Int>> =
        userRepository.getMyProfileFromLocal(profileId).flatMapLatest { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data == null) {
                        flow {
                            userRepository.refreshMyProfile(profileId)
                            val result2 = userRepository.getMyProfileFromLocal(profileId).first()
                            if (result2 is Result.Success) {
                                emit(result2.data?.categories ?: emptyList())
                            }
                        }
                    } else {
                        flowOf(result.data.categories)
                    }
                }
                is Result.Error -> { flowOf(emptyList()) }
                Result.Loading -> { flowOf(emptyList()) }
            }
        }
            .catch { flowOf(emptyList<Int>()) }
}