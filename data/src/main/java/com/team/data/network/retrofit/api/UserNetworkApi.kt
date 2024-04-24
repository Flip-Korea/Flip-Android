package com.team.data.network.retrofit.api

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/** applied Interceptor & Authentication **/
interface UserNetworkApi {

    /** API-007 (사용자 프로필 조회) **/
    @GET("/api/v1/profile/{profile_id}")
    suspend fun getProfile(
        @Path("profile_id") profileId: String
    ): Response<ProfileResponse>

    /** API-011 (관심분야 카테고리 선택, 초기 설정 시에만 사용) **/
    @POST("/api/v1/profile/{profile_id}/category")
    suspend fun selectMyCategory(
        @Path("profile_id") profileId: String,
        @Body category: CategoryRequest
    ): Response<Unit>

    /** API-012 (관심분야 카테고리 변경) **/
    @PUT("/api/v1/profile/{profile_id}/category")
    suspend fun updateMyCategory(
        @Path("profile_id") profileId: String,
        @Body category: CategoryRequest,
        @Header("Authorization") accessToken: String
    ): Response<Unit>
}