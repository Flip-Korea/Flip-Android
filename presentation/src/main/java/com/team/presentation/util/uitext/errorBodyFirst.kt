package com.team.presentation.util.uitext

import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType

/**
 * 에러바디(message)를 먼저 내보내고 만약 Null이라면 사전 정의된 ErrorType를 내보낸다.
 * ##### 주의사항:
 * [ErrorBody.errors]는 제외 된 상태
 */
//TODO: 더 다양한 처리가 가능하게끔 리팩토링 필요
fun errorBodyFirst(
    errorBody: ErrorBody?,
    error: ErrorType
): UiText {
    return errorBody?.let { errBody ->
        UiText.DynamicString(errBody.message)
    } ?: error.asUiText()
}