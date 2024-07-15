package com.team.domain.usecase.post

import com.team.domain.model.post.PostList
import com.team.domain.repository.PostRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.FlipPagination
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    /**
     * 모든 Flip(post)을 네트워크를 통해서 가져오는 UseCase
     *
     * @param cursor 페이지네이션을 위한 커서
     */
    operator fun invoke(
        cursor: String?
    ): Flow<Result<PostList, ErrorType>> =
        postRepository.getPostsPagination(
            cursor,
            FlipPagination.PAGE_SIZE
        )
}