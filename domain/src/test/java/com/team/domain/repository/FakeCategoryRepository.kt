package com.team.domain.repository

import com.team.domain.model.category.Category
import com.team.domain.usecase.category.categoriesTestData
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeCategoryRepository(
    private val hasLocalData: Boolean,
    private val isNetworkError: Boolean,
): CategoryRepository {

    private val categories = MutableStateFlow(emptyList<Category>())

    override fun getCategoriesFromLocal(): Flow<List<Category>> = categories

    override suspend fun refreshCategories(): Result<Boolean, ErrorType> {
        if (isNetworkError) {
            val networkError: Result<List<Category>, ErrorType> = Result.Error(ErrorType.Network.FORBIDDEN)
            categories.update { emptyList() }
            return Result.Error(ErrorType.Network.UNEXPECTED)
        } else {
            val networkCategories: Result<List<Category>, ErrorType> = Result.Success(categoriesTestData)
            categories.update { (networkCategories as Result.Success).data }
            return Result.Success(true)
        }
    }
}