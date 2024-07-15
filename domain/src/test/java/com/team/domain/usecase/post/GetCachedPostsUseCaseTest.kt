package com.team.domain.usecase.post

import com.team.domain.model.post.Post
import com.team.domain.repository.PostRepository
import com.team.domain.usecase.post.testdoubles.getPostsTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCachedPostsUseCaseTest {

    private val postRepository: PostRepository = mockk()
    private val getCachedPostsUseCase = GetCachedPostsUseCase(postRepository)

    @Test
    fun `Successful`() = runTest {
        // Given
        val expected = getPostsTestData()
        every {
            postRepository.getCachedPosts()
        } returns flowOf(expected)

        // When
        val actual = getCachedPostsUseCase().first()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Failure`() = runTest {
        // Given
        val expected = emptyList<Post>()
        every {
            postRepository.getCachedPosts()
        } returns flowOf(expected)

        // When
        val actual = getCachedPostsUseCase().first()

        // Then
        assertEquals(expected, actual)
    }
}