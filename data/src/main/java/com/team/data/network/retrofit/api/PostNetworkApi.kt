package com.team.data.network.retrofit.api

import com.team.data.network.model.request.CommentRequest
import com.team.data.network.model.request.LikeRequest
import com.team.data.network.model.request.PostRequest
import com.team.data.network.model.response.ResultIdResponse
import com.team.data.network.model.response.comment.CommentListResponse
import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.post.PostResponse
import com.team.data.network.model.response.post.TempPostListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 1. Post == Flip
 * 2. applied Interceptor & Authentication**/
interface PostNetworkApi {

    /** API-013 (모든 게시글 조회) **/
    @GET("/api/v1/post/list")
    suspend fun getPosts(
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int
    ): Response<PostListResponse>

    /** API-051 (단일 게시글 조회) **/
    @GET("/api/v1/posts/{post_id}")
    suspend fun getPostById(
        @Path("post_id") postId: Long
    ): Response<PostResponse>

    /** API-014 (게시글 작성) **/
    @POST("/api/v1/post")
    suspend fun addPost(
        @Body postRequest: PostRequest
    ): Response<ResultIdResponse>

    /** API-052 (게시글 편집) **/
    @PATCH("/api/v1/posts/{post_id}")
    suspend fun editPost(
        @Body postRequest: PostRequest
    ): Response<Unit>

    /** API-015 (카테고리, 회원, 태그 별 게시글 조회) **/
    //TODO type_id 타입이 각기 다른 문제
    // 카테고리(Int), 회원(String), 태그(String)
    // 1. 제일 쉬운 방법은 API 분리
    @GET("/api/v1/post/list/{type}/{type_id}")
    suspend fun getPostsByType(
        @Path("type") type: String,
        @Path("type_id") typeId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int
    ): Response<PostListResponse>

    /** API-017 (게시글 삭제) **/
    @DELETE("/api/v1/post/{post_id}")
    suspend fun deletePost(
        @Path("post_id") postId: Long
    ): Response<Unit>

    /** API-021 (특정 분야(카테고리)에서 인기 플리퍼 게시글 조회)
     *
     * 처음 호출 시 limit = 2**/
    @GET("/api/v1/post/list/{category_id}/popular")
    suspend fun getPostsByPopularUser(
        @Path("category_id") categoryId: Int,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int
    ): Response<PostListResponse>

    /** API-023 (댓글 조회) **/
    @GET("/api/v1/post/{post_id}/comment")
    suspend fun getComments(
        @Path("post_id") postId: Long,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int
    ): Response<CommentListResponse>

    /** API-024 (댓글 작성) **/
    @POST("/api/v1/post/{post_id}/comment")
    suspend fun addComment(
        @Path("post_id") postId: Long,
        @Body commentRequest: CommentRequest
    ): Response<ResultIdResponse>

    /** API-029 (댓글 삭제(본인만))
     *
     * TODO 본인 댓글인 지 판단 필요 **/
    @DELETE("/api/v1/post/comment/{comment_id}")
    suspend fun deleteComment(
        @Path("comment_id") commentId: Long
    ): Response<Unit>

    /** API-027 (게시글 좋아요) **/
    @POST("/api/v1/post/like")
    suspend fun likePost(
        @Body likeRequest: LikeRequest
    ): Response<ResultIdResponse>

    /** API-028 (게시글 좋아요 취소) **/
    @POST("/api/v1/post/unlike")
    suspend fun unLikePost(
        @Body likeRequest: LikeRequest
    ): Response<Unit>

    /** API-032 (임시저장 게시글 추가) **/
    @POST("/api/v1/post/temporary")
    suspend fun addTemporaryPost(
        @Body postRequest: PostRequest
    ): Response<ResultIdResponse>

    /** API-033 (임시저장 게시글 삭제) **/
    @DELETE("/api/v1/post/temporary/{temp_post_id}")
    suspend fun deleteTemporaryPost(
        @Path("temp_post_id") tempPostId: Long
    ): Response<Unit>

    /** API-034 (임시저장 게시글 조회) **/
    @GET("/api/v1/post/list/temporary/{profile_id}")
    suspend fun getTemporaryPosts(
        @Path("profile_id") profileId: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<TempPostListResponse>

    /** API-035 (임시저장 게시글 편집) **/
    @PUT("/api/v1/post/temporary")
    suspend fun editTemporaryPost(
        @Body postRequest: PostRequest
    ): Response<Unit>
}