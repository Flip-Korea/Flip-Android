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
        const val MIN_LENGTH = 2
        const val MAX_LENGTH = 16
    }
}

// 정규 표현식: 영문, 허용 문자 (., _) 만 허용
private val FlipIdRegex = "^[a-zA-Z._]+\$".toRegex()
