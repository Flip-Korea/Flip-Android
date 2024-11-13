package com.team.domain.util.validation

sealed interface ValidationErrorType {

    enum class Post: ValidationErrorType {
        /** 제목이 빈 경우 */
        TITLE_IS_EMPTY,
        /** 내용이 빈 경우 */
        CONTENT_IS_EMPTY,
        /** 내용이 너무 긴 경우 */
        CONTENT_TOO_LONG,
        /** 태그 개수가 10개 초과한 경우 */
        TAGS_10_LIMIT,
        /** 태그 중에 빈 문자열이 있는 경우 */
        TAGS_EMPTY_ITEM,
    }

    enum class TempPost: ValidationErrorType {
        EMPTY_TITLE_AND_CONTENT
    }
}