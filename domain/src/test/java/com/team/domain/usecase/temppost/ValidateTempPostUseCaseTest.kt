package com.team.domain.usecase.temppost

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult
import org.junit.Assert.*
import org.junit.Test

class ValidateTempPostUseCaseTest {

    private val validateTempPostUseCase = ValidateTempPostUseCase()

    @Test
    fun `TempPost 유효성 검사 (제목만 빈 경우)`() {
        val validationResult = validateTempPostUseCase("", listOf("a"))
        assert(validationResult is ValidationResult.Success)
    }

    @Test
    fun `TempPost 유효성 검사 (내용만 빈 경우)`() {
        val validationResult = validateTempPostUseCase("title", listOf(""))
        assert(validationResult is ValidationResult.Success)
    }

    @Test
    fun `TempPost 유효성 검사 (제목과 내용 모두 채워진 경우)`() {
        val validationResult = validateTempPostUseCase("title", listOf("content"))
        assert(validationResult is ValidationResult.Success)
    }

    @Test
    fun `TempPost 유효성 검사 (제목과 내용 모두 채워지지 않은 경우)`() {
        val validationResult = validateTempPostUseCase("", listOf(""))
        assertEquals(
            (validationResult as ValidationResult.Error).error,
            ValidationErrorType.TempPost.EMPTY_TITLE_AND_CONTENT
        )
    }
}