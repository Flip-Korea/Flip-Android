package com.team.domain.usecase.category

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

private interface MockRepository {
    fun getNumber(): Int
}

private class MockTest(private val mockRepository: MockRepository) {
    operator fun invoke(): Boolean =
        mockRepository.getNumber() == 10
}

@ExperimentalCoroutinesApi
class MockKTest {

    /**
     * When Coroutine Test, Use coEvery { ... }
     */

    private val mockRepository: MockRepository = mockk()
    private val mockTest = MockTest(mockRepository)

    @Test
    fun `MockK Success Test`() = runTest {
        // Given
        every {
            mockRepository.getNumber()
        } returns 10

        // When
       val result = mockTest()

        // Then
        verify(exactly = 1) { mockRepository.getNumber() }
        assertEquals(result, true)
    }

    @Test
    fun `MockK Failure Test`() = runTest {
        // Given
        every {
            mockRepository.getNumber()
        } returns 5

        // When
        val result = mockTest()

        // Then
        verify(exactly = 1) { mockRepository.getNumber() }
        assertEquals(result, false)
    }
}