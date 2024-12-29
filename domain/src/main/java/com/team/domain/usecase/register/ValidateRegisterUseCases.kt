package com.team.domain.usecase.register

import javax.inject.Inject

class ValidateRegisterUseCases @Inject constructor(
    val validateInputNameUseCase: ValidateInputNameUseCase,
    // TODO: 다른 UseCase 들 모아 놓을 예정
)
