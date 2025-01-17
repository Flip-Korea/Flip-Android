package com.team.presentation.register.di

import com.team.domain.usecase.register.ValidateInputIdUseCase
import com.team.domain.usecase.register.ValidateInputNicknameUseCase
import com.team.domain.usecase.register.ValidateRegisterUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RegisterModule {
    @Provides
    @ViewModelScoped
    fun provideValidateInputNameUseCase(): ValidateInputNicknameUseCase =
        ValidateInputNicknameUseCase()

    @Provides
    @ViewModelScoped
    fun provideValidateInputIdUseCase(): ValidateInputIdUseCase = ValidateInputIdUseCase()

    @Provides
    @ViewModelScoped
    fun provideValidateRegisterUseCases(
        validateInputNicknameUseCase: ValidateInputNicknameUseCase,
        validateInputIdUseCase: ValidateInputIdUseCase,
    ): ValidateRegisterUseCases =
        ValidateRegisterUseCases(
            validateInputNicknameUseCase = validateInputNicknameUseCase,
            validateInputIdUseCase = validateInputIdUseCase,
        )
}
