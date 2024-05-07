package com.hossam.mobilemasrtask.di

import com.hossam.mobilemasrtask.auth.domain.use_case.ValidateEmailUseCase
import com.hossam.mobilemasrtask.auth.domain.use_case.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {



    @Provides
    @ViewModelScoped
    fun provideValidateEmailUseCase() = ValidateEmailUseCase()


    @Provides
    @ViewModelScoped
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()
}