package com.team.presentation.register.di

import com.team.domain.usecase.register.ValidateInputNameUseCase
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
    fun provideValidateInputNameUseCase(): ValidateInputNameUseCase = ValidateInputNameUseCase()

    @Provides
    @ViewModelScoped
    fun provideValidateRegisterUseCases(
        validateInputNameUseCase: ValidateInputNameUseCase,
    ): ValidateRegisterUseCases =
        ValidateRegisterUseCases(
            validateInputNameUseCase = validateInputNameUseCase,
        )
}
