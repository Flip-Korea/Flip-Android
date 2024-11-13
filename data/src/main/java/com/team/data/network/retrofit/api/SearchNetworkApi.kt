package com.team.data.network.retrofit.api

import com.team.data.network.model.response.post.PostListResponse
import com.team.data.network.model.response.profile.DisplayProfileListResponse
import com.team.data.network.model.response.tag.TagListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchNetworkApi {

    /** API-036 (게시글 내용 검색) **/
    @GET("/api/v1/search/posts/{search_query}")
    suspend fun searchByPost(
        @Path("search_query") searchQuery: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<PostListResponse>

    /** API-037 (이름 검색) **/
    @GET("/api/v1/search/nickname/{search_query}")
    suspend fun searchByNickname(
        @Path("search_query") searchQuery: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<DisplayProfileListResponse>

    /** API-038 (태그 검색) **/
    @GET("/api/v1/search/tag/{search_query}")
    suspend fun searchByTag(
        @Path("search_query") searchQuery: String,
        @Query("cursor") cursor: String,
        @Query("limit") limit: Int,
    ): Response<TagListResponse>
}