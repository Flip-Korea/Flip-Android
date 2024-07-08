package com.team.data.network.source

import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.networkCall
import com.team.data.network.retrofit.api.CategoryNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

class CategoryNetworkDataSourceImpl(
    private val categoryNetworkApi: CategoryNetworkApi
): CategoryNetworkDataSource {

    override suspend fun getCategories(): Result<List<CategoryResponse>, ErrorType> =
        networkCall { categoryNetworkApi.getCategories() }
}