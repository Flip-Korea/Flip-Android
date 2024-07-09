package com.team.data.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.category.CategoryResponse
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface InterestCategoryNetworkDataSource {

    suspend fun getMyCategories(): Result<List<CategoryResponse>, ErrorType>

    suspend fun updateMyCategories(categoryIds: CategoryRequest): Result<Boolean, ErrorType>
}