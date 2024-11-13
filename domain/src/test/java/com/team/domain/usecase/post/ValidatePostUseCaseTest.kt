package com.team.domain.usecase.post

import com.team.domain.type.BackgroundColorType
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
        val bgColorType = BackgroundColorType.DEFAULT
        val tags = emptyList<String>()
        val categoryId = 2

        // When
        val validationResult = validatePostUseCase(title, content, tags)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.TITLE_IS_EMPTY
        )
    }

    @Test
    fun `Post 유효성 검사 (내용이 빈 경우)`() {
        // Given
        val title = "title"
        val content = listOf("")
        val bgColorType = BackgroundColorType.DEFAULT
        val tags = emptyList<String>()
        val categoryId = 2

        // When
        val validationResult = validatePostUseCase(title, content, tags)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.CONTENT_IS_EMPTY
        )
    }

    @Test
    fun `Post 유효성 검사 (내용 중에 500자를 초과 하는 내용이 있는 경우)`() {
        // Given
        val title = "title"
        val longContent = "a".repeat(501)
        val content = listOf(longContent)
        val bgColorType = BackgroundColorType.DEFAULT
        val tags = emptyList<String>()
        val categoryId = 2

        // When
        val validationResult = validatePostUseCase(title, content, tags)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.CONTENT_TOO_LONG
        )
    }

    @Test
    fun `Post 유효성 검사 (태그 수가 10개를 초과 하는 경우)`() {
        // Given
        val title = "title"
        val content = listOf("content")
        val bgColorType = BackgroundColorType.DEFAULT
        val tags = List(11) { "a" }
        val categoryId = 2

        // When
        val validationResult = validatePostUseCase(title, content, tags)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.TAGS_10_LIMIT
        )
    }

    @Test
    fun `Post 유효성 검사 (태그 중에 빈 문자열의 태그가 있는 경우)`() {
        // Given
        val title = "title"
        val content = listOf("content")
        val bgColorType = BackgroundColorType.DEFAULT
        val tags = listOf("a", "")
        val categoryId = 2

        // When
        val validationResult = validatePostUseCase(title, content, tags)

        // Then
        assertEquals(
            (validationResult.first() as ValidationResult.Error).error,
            ValidationErrorType.Post.TAGS_EMPTY_ITEM
        )
    }

    @Test
    fun `Post 유효성 검사 성공`() {
        // Given
        val title = "title"
        val content = listOf("content")
        val bgColorType = BackgroundColorType.DEFAULT
        val tags = listOf("sampleTag")
        val categoryId = 2

        // When
        val validationResult = validatePostUseCase(title, content, tags)

        // Then
        assertEquals(
            validationResult.first(),
            ValidationResult.Success
        )
    }
}