package com.team.data.network.retrofit.api

import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    ): Response<Unit>

    /** API-043 (스크랩 목록 조회) **/
    @GET("/api/v1/profile/{profile_id}/scrap")
    suspend fun getScrapList(
        @Path("profile_id") profileId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int
    ): Response<PostListResponse>

    /** API-044 (스크랩 코멘트 수정) **/
    @PATCH("/api/v1/profile/{profile_id}/scrap/{scrap_id}")
    suspend fun editScrapComment(
        @Path("profile_id") profileId: String,
        @Path("scrap_id") scrapId: Long,
        @Body scrapCommentRequest: ScrapCommentRequest
    ): Response<Unit>

    /** API-030 (스크랩 추가) **/
    @POST("/api/v1/post/scrap")
    suspend fun addScrap(
        @Body scrapRequest: ScrapRequest
    ): Response<ResultIdResponse>

    /** API-031 (스크랩 삭제) **/
    @DELETE("/api/v1/post/scrap/{scrap_id}")
    suspend fun deleteScrap(
        @Path("scrap_id") scrapId: Long
    ): Response<Unit>

    /**  **/
}