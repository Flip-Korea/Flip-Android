package com.team.domain.usecase.post

import com.team.domain.model.category.Category
import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidatePostUseCaseTest {
    private val validatePostUseCase = ValidatePostUseCase()

    @Test
    fun `Post 유효성 검사 (제목이 빈 경우)`() {
        // Given
        val title = ""
        val content = listOf("content")

        // When
        val validationResult = validatePostUseCase(title, content, null)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.TITLE_IS_EMPTY,
        )
    }

    @Test
    fun `Post 유효성 검사 (내용이 빈 경우)`() {
        // Given
        val title = "title"
        val content = listOf("")

        // When
        val validationResult = validatePostUseCase(title, content, null)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.CONTENT_IS_EMPTY,
        )
    }

    @Test
    fun `Post 유효성 검사 (내용 중에 500자를 초과 하는 내용이 있는 경우)`() {
        // Given
        val title = "title"
        val longContent = "a".repeat(501)
        val content = listOf(longContent)

        // When
        val validationResult = validatePostUseCase(title, content, null)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.CONTENT_TOO_LONG,
        )
    }

    @Test
    fun `Category 유효성 검사 (카테고리가 Null 일 경우)`() {
        // Given
        val title = "title"
        val longContent = "a".repeat(400)
        val content = listOf(longContent)

        // When
        val validationResult = validatePostUseCase(title, content, null)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.CATEGORY_IS_NULL,
        )
    }

    @Test
    fun `Post 유효성 검사 성공`() {
        // Given
        val title = "title"
        val content = listOf("content")
        val category = Category(1, "일상")

        // When
        val validationResult = validatePostUseCase(title, content, category)

        // Then
        assertEquals(validationResult.first(), ValidationResult.Success)
    }
}
