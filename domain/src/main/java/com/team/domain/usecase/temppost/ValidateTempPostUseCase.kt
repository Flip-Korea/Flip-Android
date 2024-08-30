package com.team.domain.usecase.temppost

import com.team.domain.type.FlipContentSeparator
import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult

class ValidateTempPostUseCase {

    /**
     * Flip(Post) 글 임시저장 시 유효성 검사
     */
    operator fun invoke(
        title: String,
        contents: List<String>,
    ): ValidationResult {

        /**
         * 만약 제목과 본문이 모두 비어 있다면,
         * 임시저장하는 의미가 없다.
         */
        return if (tempPostCondition(title, contents)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(ValidationErrorType.TempPost.EMPTY_TITLE_AND_CONTENT)
        }
    }
}

/** 임시 조건 */
fun tempPostCondition(title: String, contents: List<String>): Boolean {
    return title.isNotEmpty() || contents.joinToString(FlipContentSeparator.separator).isNotEmpty()
}