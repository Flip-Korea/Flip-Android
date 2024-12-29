package com.team.domain.usecase.register

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateInputNameUseCaseTest {
    private val validateInputNameUseCase = ValidateInputNameUseCase()

    @Test
    fun `이름이 영문 혹은 한글이고 길이가 2~12 사이일 때 성공 테스트`() {
        val validationResult = validateInputNameUseCase(VALID_LENGTH_NAME)

        assert(validationResult is ValidationResult.Success)
    }

    @Test
    fun `이름이 영문 혹은 한글이고 길이가 2~12 사이가 아닐 때 에러 테스트`() {
        val validationResult = validateInputNameUseCase(INVALID_LENGTH_NAME)

        assert(validationResult is ValidationResult.Error)
        assertEquals(
            (validationResult as ValidationResult.Error).error,
            ValidationErrorType.Register.NAME_INVALID,
        )
    }

    companion object {
        private const val VALID_LENGTH_NAME = "Aa이름"
        private const val INVALID_LENGTH_NAME = "Aa이름asdasdasdadadasdasdasdasdasdasdasdasd"
    }
}
