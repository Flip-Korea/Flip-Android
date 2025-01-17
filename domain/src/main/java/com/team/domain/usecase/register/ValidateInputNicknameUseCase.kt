package com.team.domain.usecase.register

import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult

/** 회원가입 시 이름 입력 유효성 검사 UseCase */
class ValidateInputNicknameUseCase {
    operator fun invoke(nickname: String): ValidationResult {
        val regexResult = FlipNicknameRegex.matches(nickname)
        if (nickname.length in MIN_LENGTH..MAX_LENGTH && regexResult) {
            return ValidationResult.Success
        }
        return ValidationResult.Error(ValidationErrorType.Register.NICKNAME_INVALID)
    }

    companion object {
        const val MIN_LENGTH = 2
        const val MAX_LENGTH = 12
    }
}

// 정규 표현식: 한글([가-힣]) 및 영문([a-zA-Z])만 허용
private val FlipNicknameRegex = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s]{2,12}\$".toRegex()
