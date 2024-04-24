package com.team.data.testdoubles.network.source

import com.team.data.network.model.response.ResponseCategoryWrapper
import com.team.data.network.retrofit.api.CategoryNetworkApi
import com.team.data.network.source.CategoryNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCategoryNetworkDataSource(
    private val categoryNetworkApi: CategoryNetworkApi
): CategoryNetworkDataSource {

    override suspend fun getCategories(): Flow<Result<ResponseCategoryWrapper, ErrorType>> =
        flow {
            val result = categoryNetworkApi.getCategories()
            emit(Result.Success(result.body()!!))
        }
}