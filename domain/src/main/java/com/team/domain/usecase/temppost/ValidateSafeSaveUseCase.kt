package com.team.domain.usecase.temppost

import com.team.domain.util.SafeSaveResult

class ValidateSafeSaveUseCase {
    /** Flip(Post) 글 작성 중 뒤로 가기 시 안전 저장 */
    operator fun invoke(
        title: String,
        contents: List<String>,
    ): SafeSaveResult {
        return if (title.isNotEmpty() || contents.any { it.isNotEmpty() }) {
            SafeSaveResult.CanSave
        } else {
            SafeSaveResult.Discard
        }
    }
}
