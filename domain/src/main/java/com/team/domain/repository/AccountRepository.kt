package com.team.domain.repository

import com.team.domain.model.account.Login
import com.team.domain.model.account.NicknameValidation
import com.team.domain.model.account.ProfileIdValidation
import com.team.domain.model.account.Register
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    /**
     * 응답 설명
     * 1. Success(true): 닉네임 사용가능
     * 2. Error: (400, Bad Request): 닉네임 유효성 검사 실패
     */
    fun validateNickname(nicknameValidation: NicknameValidation): Flow<Result<Boolean, ErrorType>>

    /**
     * 응답 설명
     * 1. Success(true): ProfileId 사용가능
     * 2. Error: (400, Bad Request): ID 유효성 검사 실패
     */
    fun validateProfileId(
        profileIdValidation: ProfileIdValidation,
    ): Flow<Result<Boolean, ErrorType>>

    /**
     * 응답 설명
     * 1. Success(true): 로그인 성공
     * 2. Error: (404, Not Found): 계정을 찾을 수 없음, 회원가입 진행 (403, Forbidden): 정지된 계정 혹은 접근 불가*
     */
    fun login(login: Login): Flow<Result<Boolean, ErrorType>>

    /**
     * 응답 설명
     * 1. Success(true): 로그인 성공
     * 2. Error: (400, Bad Request): 유효하지 않은 요청*
     */
    fun register(register: Register): Flow<Result<Boolean, ErrorType>>
}
