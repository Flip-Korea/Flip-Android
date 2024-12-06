package com.team.domain.util.validation

sealed interface ValidationErrorType {
    enum class Post : ValidationErrorType {
        /** 제목이 빈 경우 */
        TITLE_IS_EMPTY,

        /** 내용이 빈 경우 */
        CONTENT_IS_EMPTY,

        /** 내용이 너무 긴 경우 */
        CONTENT_TOO_LONG,

        /** 카테고리를 선택하지 않았을 경우 */
        CATEGORY_IS_NULL,
    }

    enum class TempPost : ValidationErrorType {
        EMPTY_TITLE_AND_CONTENT,
    }
}
