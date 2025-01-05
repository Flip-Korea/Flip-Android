package com.team.domain.usecase.register

import javax.inject.Inject

/** 회원가입 시 유효성 검사 UseCases */
class ValidateRegisterUseCases @Inject constructor(
    val validateInputNicknameUseCase: ValidateInputNicknameUseCase,
    val validateInputIdUseCase: ValidateInputIdUseCase,
)
