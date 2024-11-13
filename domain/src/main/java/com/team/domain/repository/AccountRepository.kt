package com.team.domain.repository

import com.team.domain.model.account.Account
import com.team.domain.model.account.Register
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun changeProfile(profileId: String): Flow<Result<Boolean, ErrorType>>

    /** Response Account Data is My Account **/
    fun getUserAccount(): Flow<Result<Account, ErrorType>>

    /** 응답 설명
     *
     * 1. Success(true): 닉네임 사용가능
     * 2. Error:
     *  (409, Conflict): 닉네임 이미 사용중
     *  (400, Bad Request): 닉네임 형식 오류**/
    fun checkDuplicateName(nickname: String): Flow<Result<Boolean, ErrorType>>

    /** 응답 설명
     *
     * 1. Success(true): ProfileId 사용가능
     * 2. Error:
     *  (409, Conflict): ProfileId 이미 사용중
     *  (400, Bad Request): ProfileId 형식 오류**/
    fun checkDuplicateProfileId(profileId: String): Flow<Result<Boolean, ErrorType>>

    /** 응답 설명
     *
     * 1. Success(true): 로그인 성공
     * 2. Error:
     *  (404, Not Found): 계정을 찾을 수 없음, 회원가입 진행
     *  (403, Forbidden): 정지된 계정 혹은 접근 불가**/
    fun login(
        loginPlatformType: SocialLoginPlatform,
        accountId: String
    ): Flow<Result<Boolean, ErrorType>>


    /** 응답 설명
     *
     * 1. Success(true): 로그인 성공
     * 2. Error:
     *  (400, Bad Request): 유효하지 않은 요청**/
    fun register(register: Register): Flow<Result<Boolean, ErrorType>>
}