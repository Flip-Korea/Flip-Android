package com.team.domain.usecase.temppost

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
class DeleteTempPostUseCaseTest {

    private val tempPostRepository: TempPostRepository = mockk()
    private val deleteTempPostUseCase = DeleteTempPostUseCase(tempPostRepository)

    @Test
    fun `임시 저장 된 Flip(Post) 삭제 실패`() = runTest {
        // Given
        val tempPostId: Long = 1
        val actualError = ErrorType.Network.BAD_REQUEST
        every {
            tempPostRepository.deleteTemporaryPost(tempPostId)
        } returns flowOf(Result.Error(actualError))

        // When
        val result = deleteTempPostUseCase(tempPostId).first()
        val expectedError = (result as Result.Error).error

        // Then
        assertEquals(actualError, expectedError)
    }

    @Test
    fun `임시 저장 된 Flip(Post) 삭제 성공`() = runTest {
        // Given
        val tempPostId: Long = 1
        every {
            tempPostRepository.deleteTemporaryPost(tempPostId)
        } returns flowOf(Result.Success(true))

        // When
        val result = deleteTempPostUseCase(tempPostId).first()

        // Then
        assert((result as Result.Success).data)
    }
}