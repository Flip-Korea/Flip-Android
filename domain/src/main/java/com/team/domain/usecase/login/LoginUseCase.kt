package com.team.domain.usecase.login

import com.team.domain.model.account.Login
import com.team.domain.repository.AccountRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** 소셜로그인 Usecase */
class LoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(login: Login): Flow<Result<Boolean, ErrorType>> =
        accountRepository.login(login)
}
