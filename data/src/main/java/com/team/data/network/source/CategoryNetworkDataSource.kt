package com.team.data.network.source

import com.team.data.network.model.response.category.CategoryResponseWrapper
import com.team.domain.util.ErrorType
import com.team.domain.util.Result

interface CategoryNetworkDataSource {

    suspend fun getCategories(): Result<CategoryResponseWrapper, ErrorType>
}