package com.team.domain.usecase.temppost

import com.team.domain.model.post.NewPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FlipContentSeparator
import com.team.domain.type.FontStyleType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddTempPostUseCase @Inject constructor(
    private val tempPostRepository: TempPostRepository
) {

    /**
     * Flip(Post)를 임시저장 한다.
     */
    operator fun invoke(
        title: String,
        content: List<String>,
        bgColorType: BackgroundColorType,
        fontStyleType: FontStyleType = FontStyleType.NORMAL,
        tags: List<String>,
        categoryId: Int?
    ): Flow<Result<Boolean, ErrorType>> {
        val newPost = NewPost(
            title = title,
            content = content.joinToString(FlipContentSeparator.SEPARATOR),
            bgColorType = bgColorType,
            fontStyleType = fontStyleType,
            tags = tags,
            categoryId = categoryId
        )
        return tempPostRepository.addTemporaryPost(newPost)
    }
}