package com.team.domain.usecase.register

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateInputIdUseCaseTest {
    private val validateInputIdUseCase = ValidateInputIdUseCase()

    @Test
    fun `ID가 영문 또는 허용 문자로 구성 되어 있고, 길이가 2 ~ 16글자 사이일 때 성공 테스트`() {
        val validationResult = validateInputIdUseCase(VALID_ID)

        assert(validationResult is ValidationResult.Success)
    }

    @Test
    fun `ID가 영문 또는 허용 문자로 구성 되어 있지 않을 때 에러 테스트`() {
        val validationResult = validateInputIdUseCase(INVALID_ID)

        assert(validationResult is ValidationResult.Error)
        assertEquals(
            (validationResult as ValidationResult.Error).error,
            ValidationErrorType.Register.ID_INVALID,
        )
    }

    @Test
    fun `ID 길이가 2 ~ 16글자 사이가 아닐 때 에러 테스트`() {
        val validationResult = validateInputIdUseCase(INVALID_ID_2)

        assert(validationResult is ValidationResult.Error)
        assertEquals(
            (validationResult as ValidationResult.Error).error,
            ValidationErrorType.Register.ID_INVALID,
        )

        val validationResult2 = validateInputIdUseCase(INVALID_ID_3)

        assert(validationResult2 is ValidationResult.Error)
        assertEquals(
            (validationResult2 as ValidationResult.Error).error,
            ValidationErrorType.Register.ID_INVALID,
        )
    }

    companion object {
        private const val VALID_ID = "hello.__.aaa"
        private const val INVALID_ID = "안녕123"
        private const val INVALID_ID_2 = "z"
        private const val INVALID_ID_3 = "zxczxczxczxczxczczczxczxczxczxczczczxczxcz"
    }
}
