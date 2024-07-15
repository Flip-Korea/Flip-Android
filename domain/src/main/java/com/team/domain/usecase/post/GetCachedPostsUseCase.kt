package com.team.domain.usecase.post

import com.team.domain.model.post.Post
import com.team.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    /**
     * Local DB 에서 Flip(Post)을 가져오는 UseCase
     */
    operator fun invoke(): Flow<List<Post>> = postRepository.getCachedPosts()
}