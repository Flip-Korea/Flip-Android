package com.team.data.network.source.fake

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.retrofit.api.InterestCategoryNetworkApi
import com.team.data.network.source.InterestCategoryNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakeInterestCategoryNetworkDataSource(
    private val interestCategoryNetworkApi: InterestCategoryNetworkApi
): InterestCategoryNetworkDataSource {

    override suspend fun getMyCategories(): Result<List<CategoryResponse>, ErrorType> {
        val result = interestCategoryNetworkApi.getMyCategories()
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }

    override suspend fun updateMyCategories(categoryIds: CategoryRequest): Result<Boolean, ErrorType> {
        val result = interestCategoryNetworkApi.updateMyCategories(categoryIds)
        return if (result.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}