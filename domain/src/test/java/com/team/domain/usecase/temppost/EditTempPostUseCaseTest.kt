package com.team.domain.usecase.temppost

import com.team.domain.model.post.NewPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
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
class EditTempPostUseCaseTest {

    private val tempPostRepository: TempPostRepository = mockk()
    private val editTempPostUseCase = EditTempPostUseCase(tempPostRepository)

    @Test
    fun `임시 저장 한 Flip(Post) 수정 실패`() = runTest {
        // Given
        val tempPostId: Long = 1
        val newPost = NewPost(
            title = "title",
            content = "content",
            bgColorType = BackgroundColorType.DEFAULT,
            fontStyleType = FontStyleType.NORMAL,
            tags = listOf(""),
            categoryId = 0,
        )
        val actualError = ErrorType.Network.FORBIDDEN
        every {
            tempPostRepository.editTemporaryPost(tempPostId, newPost)
        } returns flowOf(Result.Error(actualError))

        // When
        val result = editTempPostUseCase(
            tempPostId = tempPostId,
            title = newPost.title,
            content = newPost.content,
            bgColorType = newPost.bgColorType,
            tags = newPost.tags,
            categoryId = newPost.categoryId
        ).first()
        val expectedError = (result as Result.Error).error

        // Then
        assertEquals(actualError, expectedError)
    }

    @Test
    fun `임시 저장 한 Flip(Post) 수정 성공`() = runTest {
        // Given
        val tempPostId: Long = 1
        val newPost = NewPost(
            title = "title",
            content = "content",
            bgColorType = BackgroundColorType.DEFAULT,
            fontStyleType = FontStyleType.NORMAL,
            tags = listOf(""),
            categoryId = 0,
        )
        every {
            tempPostRepository.editTemporaryPost(tempPostId, newPost)
        } returns flowOf(Result.Success(true))

        // When
        val result = editTempPostUseCase(
            tempPostId = tempPostId,
            title = newPost.title,
            content = newPost.content,
            bgColorType = newPost.bgColorType,
            tags = newPost.tags,
            categoryId = newPost.categoryId
        ).first()

        // Then
        assert((result as Result.Success).data)
    }
}