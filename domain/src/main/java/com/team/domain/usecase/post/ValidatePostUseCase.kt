package com.team.domain.usecase.post

import com.team.domain.model.category.Category
import com.team.domain.type.FlipContentSeparator
import com.team.domain.util.validation.ValidationErrorType
import com.team.domain.util.validation.ValidationResult

private const val LETTER_HIDDEN_LIMIT = 500

class ValidatePostUseCase {
    /**
     * Flip(Post) 글 등록 시 유효성 검사
     *
     * 각 조건의 상황은 [ValidationErrorType] 에서 확인
     *
     * //TODO 비속어, 부적절한 언어 필터링 필요
     *
     * @see ValidationErrorType
     * @see LETTER_HIDDEN_LIMIT
     */
    operator fun invoke(
        title: String,
        content: List<String>,
        category: Category?,
    ): List<ValidationResult> {
        val validationResults = mutableListOf<ValidationResult>()
        val joinContents = content.joinToString(FlipContentSeparator.SEPARATOR)

        if (title.isEmpty()) {
            validationResults.add(ValidationResult.Error(ValidationErrorType.Post.TITLE_IS_EMPTY))
        }

        if (joinContents.isEmpty()) {
            validationResults.add(ValidationResult.Error(ValidationErrorType.Post.CONTENT_IS_EMPTY))
        }

        if (content.any { it.length > LETTER_HIDDEN_LIMIT }) {
            validationResults.add(ValidationResult.Error(ValidationErrorType.Post.CONTENT_TOO_LONG))
        }

        if (category == null) {
            validationResults.add(ValidationResult.Error(ValidationErrorType.Post.CATEGORY_IS_NULL))
        }

        // 어떠한 에러도 없을 시, 성공
        if (validationResults.isEmpty()) {
            validationResults.add(ValidationResult.Success)
        }

        return validationResults
    }
}
