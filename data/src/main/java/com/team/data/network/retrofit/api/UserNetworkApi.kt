package com.team.data.network.retrofit.api

import com.team.data.network.model.request.BlockRequest
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.request.EditProfileRequest
import com.team.data.network.model.request.FollowRequest
import com.team.data.network.model.request.ReportRequest
import com.team.data.network.model.request.ScrapCommentRequest
import com.team.data.network.model.request.ScrapRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.block.BlockListResponse
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.model.response.comment.MyCommentListResponse
import com.team.data.network.model.response.follow.FollowerListResponse
import com.team.data.network.model.response.follow.FollowingListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.MyProfileResponse
import com.team.data.network.model.response.profile.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    /** API-018 (계정 신고) **/
    @POST("/api/v1/profile/report")
    suspend fun reportAccount(
        @Body reportRequest: ReportRequest
    ): Response<Unit>

    /** API-019 (계정 차단) **/
    @POST("/api/v1/profile/block")
    suspend fun blockAccount(
        @Body blockRequest: BlockRequest
    ): Response<Unit>

    /** API-020 (계정 차단 해제) **/
    @DELETE("/api/v1/profile/{profile_id}/block/{blocked_id}")
    suspend fun unblockAccount(
        @Path("profile_id") profileId: String,
        @Path("blocked_id") blockedId: String
    ): Response<Unit>

    /** API-026 ((본인)프로필 수정) **/
    @PATCH("/api/v1/profile/my/{profile_id}")
    suspend fun editMyProfile(
        @Path("profile_id") profileId: String,
        @Body editProfileRequest: EditProfileRequest
    ): Response<Unit>

    /** API-039 (팔로우) **/
    @POST("/api/v1/profile/follow")
    suspend fun follow(
        @Body followRequest: FollowRequest
    ): Response<Unit>

    /** API-040 (언팔로우) **/
    @POST("/api/v1/profile/unfollow")
    suspend fun unfollow(
        @Body followRequest: FollowRequest
    ): Response<Unit>

    /** API-041 (팔로워 목록 조회) **/
    @GET("/api/v1/profile/{profile_id}/follower")
    suspend fun getFollowerList(
        @Path("profile_id") profileId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<FollowerListResponse>

    /** API-042 (팔로잉 목록 조회) **/
    @GET("/api/v1/profile/{profile_id}/following")
    suspend fun getFollowingList(
        @Path("profile_id") profileId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<FollowingListResponse>

    /** API-045 (차단 목록 조회) **/
    @GET("/api/v1/profile/{profile_id}/block")
    suspend fun getBlockList(
        @Path("profile_id") profileId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<BlockListResponse>

    /** API-046 (댓글 목록 조회) **/
    @GET("/api/v1/profile/{profile_id}/comment")
    suspend fun getMyCommentList(
        @Path("profile_id") profileId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<MyCommentListResponse>

    /** API-050(임시) (사용자 관심분야 카테고리 조회) **/
    @GET("/api/v1/profile/{profile_id}/category")
    suspend fun getMyCategories(
        @Path("profile_id") profileId: String
    ): Response<List<CategoryResponse>>

    /** API-049 (본인 프로필 조회) **/
    @GET("/api/v1/profile/my/{profile_id}")
    suspend fun getMyProfile(
        @Path("profile_id") profileId: String
    ): Response<MyProfileResponse>
}