package com.team.data.network.retrofit.api

import com.team.data.network.model.response.category.CategoryResponseWrapper
import retrofit2.Response
import retrofit2.http.GET

interface CategoryNetworkApi {

    /** API-010 (카테고리 목록 조회) **/
    @GET("/api/v1/category")
    suspend fun getCategories(): Response<CategoryResponseWrapper>
}