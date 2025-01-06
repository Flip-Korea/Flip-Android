package com.team.domain.usecase.account

import com.team.domain.model.account.NicknameValidation
import com.team.domain.repository.AccountRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNicknameValidationResultUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(nicknameValidation: NicknameValidation): Flow<Result<Boolean, ErrorType>> =
        accountRepository.validateNickname(nicknameValidation)
}
