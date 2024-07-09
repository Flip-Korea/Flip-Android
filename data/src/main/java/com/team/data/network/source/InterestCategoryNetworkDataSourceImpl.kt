package com.team.data.network.source

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.networkCall
import com.team.data.network.networkCallWithoutResponse
import com.team.data.network.retrofit.api.InterestCategoryNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class InterestCategoryNetworkDataSourceImpl(
    private val interestCategoryNetworkApi: InterestCategoryNetworkApi
): InterestCategoryNetworkDataSource {

    override suspend fun getMyCategories(): Result<List<CategoryResponse>, ErrorType> =
        networkCall { interestCategoryNetworkApi.getMyCategories() }

    override suspend fun updateMyCategories(categoryIds: CategoryRequest): Result<Boolean, ErrorType> =
        networkCallWithoutResponse { interestCategoryNetworkApi.updateMyCategories(categoryIds) }
}