package com.team.domain.usecase.post

import com.team.domain.model.post.PostList
import com.team.domain.repository.PostRepository
import com.team.domain.type.PathParameterType
import com.team.domain.util.ErrorType
import com.team.domain.util.FlipPagination
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsByTypeUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    /**
     * 타입 별로 Flip(post)을 가져오는 UseCase
     *
     * @param type 타입 구분 (카테고리, 태그, 프로필ID 등)
     * @param typeId 타입 구분에 맞는 ID
     * @param cursor 페이지네이션을 위한 커서
     */
    operator fun invoke(
        type: PathParameterType,
        typeId: String,
        cursor: String?,
    ): Flow<Result<PostList, ErrorType>> =
        postRepository.getPostsByTypePagination(
            type,
            typeId,
            cursor,
            FlipPagination.PAGE_SIZE
        )
}