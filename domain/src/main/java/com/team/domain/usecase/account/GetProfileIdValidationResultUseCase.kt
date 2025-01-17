package com.team.domain.usecase.account

import com.team.domain.model.account.ProfileIdValidation
import com.team.domain.repository.AccountRepository
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileIdValidationResultUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(
        profileIdValidation: ProfileIdValidation,
    ): Flow<Result<Boolean, ErrorType>> = accountRepository.validateProfileId(profileIdValidation)
}
