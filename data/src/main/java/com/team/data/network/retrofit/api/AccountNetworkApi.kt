package com.team.data.network.retrofit.api

import com.team.data.network.model.request.NicknameValidationRequest
import com.team.data.network.model.request.ProfileIdValidationRequest
import com.team.data.network.model.request.RegisterRequest
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/** Account API */
interface AccountNetworkApi {
    /** API-001 (사용자 계정 조회) * */
    @GET("/api/v1/account")
    suspend fun getUserAccount(
        @Header("Authorization") accessToken: String,
    ): Response<AccountResponse>

    /** API-002 (닉네임 유효성 검사) * */
    @POST("/api/v1/validations/nickname")
    suspend fun validateNickname(
        @Body nicknameValidationRequest: NicknameValidationRequest,
    ): Response<Void>

    /** API-003 (ID 유효성 검사) * */
    @POST("/api/v1/validations/user-id")
    suspend fun validateProfileId(
        @Body profileIdValidationRequest: ProfileIdValidationRequest,
    ): Response<Void>

    /**
     * API-004 (로그인)
     *
     * 첫 로그인 화면에서만 호출*
     */
    @GET("/api/v1/auth/login/{account_id}")
    suspend fun login(
        @Path("account_id") accountId: String,
    ): Response<TokenResponse>

    /** API-005 (회원가입) * */
    @POST("/api/v1/accounts")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): Response<TokenResponse>

    /**
     * API-XXX
     *
     * This API Only Called by 'TokenAuthentication' class *
     */
    @GET("/api/v1/auth/refresh")
    suspend fun tokenRefresh(
        @Header("Authorization") refreshToken: String,
    ): Response<TokenResponse>
}
