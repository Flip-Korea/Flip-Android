package com.team.data.network.retrofit.api

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.category.CategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

/**
 * 나의 관심 카테고리 API
 *
 * 요청시 AccessToken 필요
 */
interface InterestCategoryNetworkApi {

    /**
     * API-050 (나의 관심 카테고리 조회)
     */
    @GET("/api/v1/my/categories")
    suspend fun getMyCategories(): Response<List<CategoryResponse>>

    /**
     *  API-012 (나의 관심 카테고리 변경)
     *  @param categoryIds 새로 변경할 카테고리 ID 목록
     *  (제약 조건: 중복된 ID X, 3개 이상 12개 이하)
     */
    @PUT("/api/v1/my/categories")
    suspend fun updateMyCategories(
        @Body categoryIds: CategoryRequest
    ): Response<Void>
}