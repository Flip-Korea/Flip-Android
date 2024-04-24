package com.team.data.network.source

import com.team.data.network.model.response.ResponseCategoryWrapper
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface CategoryNetworkDataSource {

    suspend fun getCategories(): Flow<Result<ResponseCategoryWrapper, ErrorType>>
}