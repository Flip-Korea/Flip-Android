package com.team.data.network.source.fake

import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.retrofit.api.CategoryNetworkApi
import com.team.data.network.source.CategoryNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class FakeCategoryNetworkDataSource(
    private val categoryNetworkApi: CategoryNetworkApi,
) : CategoryNetworkDataSource {

    override suspend fun getCategories(): Result<List<CategoryResponse>, ErrorType> {
        val result = categoryNetworkApi.getCategories()
        return if (result.isSuccessful) {
            Result.Success(result.body()!!)
        } else {
            Result.Error(ErrorType.Network.UNEXPECTED)
        }
    }
}