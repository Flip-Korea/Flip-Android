package com.team.domain.usecase.post

import com.team.domain.repository.PostRepository
import com.team.domain.type.FlipContentSeparator
import com.team.domain.type.asBackgroundColorType
import com.team.domain.usecase.post.testdoubles.newPostTestData
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class AddPostUseCaseTest {

    private val postRepository: PostRepository = mockk()
    private val addPostUseCases = AddPostUseCase(postRepository)

    @Test
    fun `플립 글 추가 성공`() = runTest {
        // Given
        every {
            postRepository.addPost(newPostTestData)
        } returns flowOf(Result.Success(true))

        // When
        val result = addPostUseCases(
            newPostTestData.title,
            newPostTestData.content.split(FlipContentSeparator.separator),
            newPostTestData.bgColorType,
            newPostTestData.fontStyleType,
            newPostTestData.tags,
            newPostTestData.categoryId!!
        ).first()

        // Then
        assert((result as Result.Success).data)
    }

    @Test
    fun `플립 글 추가 실패`() = runTest {
        // Given
        val error = ErrorType.Network.BAD_REQUEST
        every {
            postRepository.addPost(newPostTestData)
        } returns flowOf(Result.Error(error = error))

        // When
        val result = addPostUseCases(
            newPostTestData.title,
            newPostTestData.content.split(FlipContentSeparator.separator),
            newPostTestData.bgColorType,
            newPostTestData.fontStyleType,
            newPostTestData.tags,
            newPostTestData.categoryId!!
        ).first()

        // Then
        assertEquals(
            (result as Result.Error).error,
            error
        )
    }
}