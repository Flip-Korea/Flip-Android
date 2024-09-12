package com.team.domain.usecase.temppost

import com.team.domain.model.post.TempPostList
import com.team.domain.repository.TempPostRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTempPostsPaginationUseCaseTest {

    private val tempPostRepository: TempPostRepository = mockk()
    private val getTempPostsUseCase = GetTempPostsPaginationUseCase(tempPostRepository)

    /** 로컬 동기화 페이지네이션 X */

    @Test
    fun `임시저장함 목록 조회 실패`() = runTest {
        // Given
        val limit = 5
        val actualError = ErrorType.Network.BAD_REQUEST
        every {
            tempPostRepository.getTempPostsPagination(null, limit)
        } returns flowOf(Result.Error(actualError))

        // When
        val result = getTempPostsUseCase(null, limit).first()
        val expectedError = (result as Result.Error).error

        // Then
        assertEquals(actualError, expectedError)
    }

    @Test
    fun `임시저장함 목록 조회 성공`() = runTest {
        // Given
        val limit = 5
        val actualTempPostList = TempPostList(emptyList(), 5)
        every {
            tempPostRepository.getTempPostsPagination(null, limit)
        } returns flowOf(Result.Success(actualTempPostList))

        // When
        val result = getTempPostsUseCase(null, limit).first()
        val expectedTempPostList = (result as Result.Success).data

        // Then
        assertEquals(actualTempPostList.totalCount, expectedTempPostList.totalCount)
    }
}