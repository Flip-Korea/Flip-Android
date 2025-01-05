package com.team.domain.usecase.register

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateInputNicknameUseCaseTest {
    private val validateInputNicknameUseCase = ValidateInputNicknameUseCase()

    @Test
    fun `이름이 영문 혹은 한글이고 길이가 2~12 사이일 때 성공 테스트`() {
        val validationResult = validateInputNicknameUseCase(VALID_LENGTH_NICKNAME)

        assert(validationResult is ValidationResult.Success)
    }

    @Test
    fun `이름이 영문 혹은 한글이고 길이가 2~12 사이가 아닐 때 에러 테스트`() {
        val validationResult = validateInputNicknameUseCase(INVALID_LENGTH_NICKNAME)

        assert(validationResult is ValidationResult.Error)
        assertEquals(
            (validationResult as ValidationResult.Error).error,
            ValidationErrorType.Register.NICKNAME_INVALID,
        )
    }

    companion object {
        private const val VALID_LENGTH_NICKNAME = "Aa이름"
        private const val INVALID_LENGTH_NICKNAME = "Aa이름asdasdasdadadasdasdasdasdasdasdasdasd"
    }
}
