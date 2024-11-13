package com.team.domain.usecase.temppost

import com.team.domain.repository.TempPostRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteTempPostUseCase @Inject constructor(
    private val tempPostRepository: TempPostRepository
) {

    /**
     * 임시 저장 Flip(Post)를 삭제한다.
     *
     * @param tempPostId 삭제 할 임시 Post ID
     */
    operator fun invoke(tempPostId: Long): Flow<Result<Boolean, ErrorType>> =
        tempPostRepository.deleteTemporaryPost(tempPostId)
}