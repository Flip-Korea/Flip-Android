package com.team.data.network.retrofit.api

import com.team.data.network.model.response.category.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * 카테고리 API
 */
interface CategoryNetworkApi {

    /** API-010 (카테고리 목록 조회) **/
    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<CategoryResponse>>
}