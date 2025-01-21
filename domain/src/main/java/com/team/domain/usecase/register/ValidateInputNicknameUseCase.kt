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

/**
 * 정규 표현식:
 * 1. 한글
 * 2. 영문 (대소문자 구분 X)
 * 3. 숫자
 * 4. 공백
 * 5. 길이 제한 2 ~ 16 ([ValidateInputNicknameUseCase.MIN_LENGTH], [ValidateInputNicknameUseCase.MAX_LENGTH])
 */
private val FlipNicknameRegex = "^[가-힣a-zA-Z0-9 ]{2,12}$".toRegex()
