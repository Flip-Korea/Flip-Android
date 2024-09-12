package com.team.domain.usecase.temppost

import com.team.domain.model.post.NewPost
import com.team.domain.repository.TempPostRepository
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditTempPostUseCase @Inject constructor(
    private val tempPostRepository: TempPostRepository
) {

    /**
     * 임시 저장 한 Flip(Post)를 수정한다.
     *
     * @param tempPostId 수정 할 임시 Post ID
     */
    operator fun invoke(
        tempPostId: Long,
        title: String,
        content: String,
        bgColorType: BackgroundColorType,
        fontStyleType: FontStyleType = FontStyleType.NORMAL,
        tags: List<String>,
        categoryId: Int?,
    ): Flow<Result<Boolean, ErrorType>> {
        val newPost = NewPost(title, content, bgColorType, fontStyleType, tags, categoryId)
        return tempPostRepository.editTemporaryPost(tempPostId, newPost)
    }
}