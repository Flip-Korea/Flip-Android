package com.team.domain.repository

import com.team.domain.model.category.Category
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    /**
     * 모든 Category를 Local DB에서 가져온다.
     *
     * 만약, 비어 있다면 Network API를 통해서 가져온다. (별도 작성 필요)
     *
     * @see refreshCategories
     */
    fun getCategoriesFromLocal(): Flow<List<Category>>

    /**
     * 모든 Category를 Network 에서 가져온다.
     *
     * Local DB에 모든 카테고리를 저장한다.
     */
    suspend fun refreshCategories(): Result<Boolean, ErrorType>
}