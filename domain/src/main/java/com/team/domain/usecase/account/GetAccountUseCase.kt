package com.team.domain.usecase.account

import com.team.domain.model.account.Account
import com.team.domain.repository.AccountRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(): Flow<Result<Account, ErrorType>> =
        accountRepository.getUserAccount()
}