package com.team.domain.usecase.register

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult

class ValidateInputNameUseCase {
    operator fun invoke(name: String): ValidationResult {
        val regexResult = KOREA_ENGLISH_REGEX.matches(name)
        if (name.length in 2..12 && regexResult) {
            return ValidationResult.Success
        }
        return ValidationResult.Error(ValidationErrorType.Register.NAME_INVALID)
    }

    companion object {
        const val MIN_LENGTH = 2
        const val MAX_LENGTH = 12
    }
}

// 정규 표현식: 한글([가-힣]) 및 영문([a-zA-Z])만 허용
private val KOREA_ENGLISH_REGEX = "^[가-힣a-zA-Z]+$".toRegex()
