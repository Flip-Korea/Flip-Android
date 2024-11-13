package com.team.presentation.addflip.di

import com.team.domain.usecase.post.ValidatePostUseCase
import com.team.domain.usecase.temppost.ValidateTempPostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class AddFlipModule {

    @Provides
    @ViewModelScoped
    fun provideValidatePostUseCase(): ValidatePostUseCase = ValidatePostUseCase()

    @Provides
    @ViewModelScoped
    fun provideValidateTempPostUseCase(): ValidateTempPostUseCase = ValidateTempPostUseCase()
}