package com.team.data.network.source

import com.team.data.network.model.response.ResponseCategoryWrapper
import com.team.data.network.networkCallFlow
import com.team.data.network.retrofit.api.CategoryNetworkApi
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

class CategoryNetworkDataSourceImpl(
    private val categoryNetworkApi: CategoryNetworkApi
): CategoryNetworkDataSource {

    override suspend fun getCategories(): Flow<Result<ResponseCategoryWrapper, ErrorType>> =
        networkCallFlow { categoryNetworkApi.getCategories() }
}