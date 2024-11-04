package com.team.domain.usecase.temppost

import androidx.paging.PagingData
import com.team.domain.model.post.TempPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.usecase.temppost.testdoubles.TempPostFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTempPostsPaginationUseCaseTest {

    private val tempPostRepository: TempPostRepository = mockk()
    private val getTempPostsUseCase = GetTempPostsPaginationUseCase(tempPostRepository)
    private val tempPostFactory = TempPostFactory()

    /** 로컬 동기화 페이지네이션 X */

    @Test
    fun `임시저장함 목록 조회 실패`() = runTest {
        // Given
        val expected = PagingData.empty<TempPost>()
        every {
            tempPostRepository.getTempPostsPagination()
        } returns flowOf(expected)

        // When
        val actual = getTempPostsUseCase().first()


        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `임시저장함 목록 조회 성공`() = runTest {
        // Given
        val limit = 5
        val list = List(limit) {
            tempPostFactory.create()
        }
        val expected = PagingData.from(list)
        every {
            tempPostRepository.getTempPostsPagination()
        } returns flowOf(expected)

        // When
        val actual = getTempPostsUseCase().first()

        // Then
        assertEquals(expected, actual)
    }
}