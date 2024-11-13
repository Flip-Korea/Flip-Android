package com.team.domain.usecase.post

import com.team.domain.repository.PostRepository
import com.team.domain.usecase.post.testdoubles.getPostListTestData
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class GetPostsUseCaseTest {

    private val postRepository: PostRepository = mockk()
    private val getPostsUseCase = GetPostsUseCase(postRepository)

    @Test
    fun `Successful`() = runTest {
        // Given
        val expected = getPostListTestData()
        every {
            postRepository.getPostsPagination(null, 15)
        } returns flowOf(Result.Success(expected))

        // When
        val actual = getPostsUseCase(null).last()

        // Then
        assertEquals(
            expected,
            (actual as Result.Success).data
        )
    }

    @Test
    fun `Failure`() = runTest {
        // Given
        val expected = ErrorType.Network.NOT_FOUND
        every {
            postRepository.getPostsPagination(null, 15)
        } returns flowOf(Result.Error(expected))

        // When
        val actual = getPostsUseCase(null).last()

        // Then
        assertEquals(
            expected,
            (actual as Result.Error).error
        )
    }
}