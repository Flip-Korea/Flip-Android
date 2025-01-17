package com.team.domain.usecase.register

import com.team.domain.model.account.Register
import com.team.domain.repository.AccountRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(register: Register): Flow<Result<Boolean, ErrorType>> =
        accountRepository.register(register)
}
