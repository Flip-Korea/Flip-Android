package com.team.presentation.util

import com.team.domain.util.validation.ValidationErrorType
import com.team.presentation.R

/**
 * [ValidationErrorType]을 [UiText] 타입으로 변환
 */
fun ValidationErrorType.asUiText(): UiText =
    when (this) {
        // Post
        ValidationErrorType.Post.TITLE_IS_EMPTY -> {
            UiText.StringResource(R.string.validation_error_post_title_is_empty)
        }

        ValidationErrorType.Post.CONTENT_IS_EMPTY -> {
            UiText.StringResource(R.string.validation_error_post_content_is_empty)
        }

        ValidationErrorType.Post.TAGS_10_LIMIT -> {
            UiText.StringResource(R.string.validation_error_post_tags_10_limit)
        }

        ValidationErrorType.Post.TAGS_EMPTY_ITEM -> {
            UiText.StringResource(R.string.validation_error_post_tags_empty_item)
        }

        ValidationErrorType.Post.CONTENT_TOO_LONG -> {
            UiText.StringResource(R.string.validation_error_post_content_too_long)
        }

        // Temp Post
        ValidationErrorType.TempPost.EMPTY_TITLE_AND_CONTENT -> {
            UiText.StringResource(R.string.validation_error_temp_post_empty_title_and_content)
        }
    }