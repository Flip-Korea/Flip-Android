package com.team.data.network.retrofit.api

import com.team.data.network.model.request.NetworkRegister
import com.team.data.network.model.response.AccountResponse
import com.team.data.network.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthNetworkApi {

    /** API-001 (사용자 계정 조회) **/
    @GET("/api/v1/account")
    suspend fun getUserAccount(
        @Header("Authorization") accessToken: String
    ): Response<AccountResponse>

    /** API-002 (이름 중복 확인) **/
    @HEAD("/api/v1/account/check-duplicate/nickname/{nickname}")
    suspend fun checkDuplicateName(
        @Path("nickname") nickname: String
    ): Response<Void>

    /** API-003 (ID 중복 확인) **/
    @HEAD("/api/v1/account/check-duplicate/id/{id}")
    suspend fun checkDuplicateAccountId(
        @Path("id") accountId: String
    ): Response<Void>

    /** API-004 (로그인) **/
    @GET("/api/v1/auth/login/{account_id}")
    suspend fun login(
        @Path("account_id") accountId: String
    ): Response<TokenResponse>

    /** API-005 (회원가입) **/
    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body networkRegister: NetworkRegister
    ): Response<TokenResponse>

    /** API-XXX
     *
     * Only Called 'TokenAuthentication' class **/
    @GET("/api/v1/auth/refresh")
    suspend fun tokenRefresh(
        @Header("Authorization") refreshToken: String
    ): Response<TokenResponse>
}