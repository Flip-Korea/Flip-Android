package com.team.domain.usecase.register

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult

/** 회원가입 시 ID 입력 유효성 검사 UseCase */
class ValidateInputIdUseCase {
    operator fun invoke(id: String): ValidationResult {
        val regexResult = FlipIdRegex.matches(id)
        if (id.length in MIN_LENGTH..MAX_LENGTH && regexResult) {
            return ValidationResult.Success
        }
        return ValidationResult.Error(ValidationErrorType.Register.ID_INVALID)
    }

    companion object {
        const val MIN_LENGTH = 4
        const val MAX_LENGTH = 16
    }
}

/**
 * 정규 표현식 설명:
 * 1. 영문(대소문자 구분 X)
 * 2. 숫자
 * 3. 허용 문자 (., _)
 * 4. 길이 제한 4 ~ 16 ([ValidateInputIdUseCase.MIN_LENGTH], [ValidateInputIdUseCase.MAX_LENGTH])
 */
private val FlipIdRegex = "^[a-zA-Z0-9._]{4,16}$".toRegex()
