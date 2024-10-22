package com.team.domain.usecase.temppost

import androidx.paging.PagingData
import com.team.domain.model.post.TempPost
import com.team.domain.model.post.TempPostList
import com.team.domain.repository.TempPostRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTempPostsPaginationUseCase @Inject constructor(
    private val tempPostRepository: TempPostRepository
) {

    /**
     * 임시 저장 한 Flip(Post) 목록을 페이지네이션을 통해 조회한다.
     */
    operator fun invoke(cursor: String?, limit: Int): Flow<PagingData<TempPost>> =
        tempPostRepository.getTempPostsPagination(cursor, limit)
}