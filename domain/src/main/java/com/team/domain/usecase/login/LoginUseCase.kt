package com.team.domain.usecase.login

import com.team.domain.repository.AccountRepository
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(
        socialLoginPlatform: SocialLoginPlatform,
        accountId: String
    ): Flow<Result<Boolean, ErrorType>> =
        accountRepository.login(socialLoginPlatform, accountId)
}