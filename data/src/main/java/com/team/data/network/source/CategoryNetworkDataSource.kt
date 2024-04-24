package com.team.data.network.source

import com.team.data.network.model.response.category.ResponseCategoryWrapper
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface CategoryNetworkDataSource {

    suspend fun getCategories(): Result<ResponseCategoryWrapper, ErrorType>
}