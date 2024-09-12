package com.team.domain.util

/**
 * Flip 에서 사용 되는 (성공했을 때 표시할 문구를 위한)SuccessType
 */
sealed interface SuccessType {
    enum class TempPost: SuccessType {
        DELETE,
    }
}