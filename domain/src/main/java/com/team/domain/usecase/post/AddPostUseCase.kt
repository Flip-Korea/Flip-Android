package com.team.domain.usecase.post

import com.team.domain.model.post.NewPost
import com.team.domain.repository.PostRepository
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FlipContentSeparator
import com.team.domain.type.asString
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    /**
     * Flip(Post)를 추가(게시) 한다.
     */
    operator fun invoke(
        title: String,
        content: List<String>,
        bgColorType: BackgroundColorType,
        fontStyleType: String = "NORMAL",
        tags: List<String>,
        categoryId: Int
    ): Flow<Result<Boolean, ErrorType>> {
        val newPost = NewPost(
            title = title,
            content = content.joinToString(FlipContentSeparator.separator),
            bgColorType = bgColorType.asString(),
            fontStyleType = fontStyleType,
            tags = tags,
            categoryId = categoryId
        )
        return postRepository.addPost(newPost)
    }
}