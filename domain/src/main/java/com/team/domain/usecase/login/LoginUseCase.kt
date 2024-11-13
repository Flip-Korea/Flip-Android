package com.team.domain.usecase.login

import com.team.domain.repository.AccountRepository
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 소셜로그인 Usecase
 */
class LoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    /**
     * @param socialLoginPlatform 소셜로그인 플랫폼 구분
     * @param accountId 각 플랫폼에서 받아온 고유식별번호(id)
     */
    operator fun invoke(
        socialLoginPlatform: SocialLoginPlatform,
        accountId: String
    ): Flow<Result<Boolean, ErrorType>> =
        accountRepository.login(socialLoginPlatform, accountId)
}