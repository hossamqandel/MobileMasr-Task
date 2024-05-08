package com.hossam.mobilemasrtask.di

import com.hossam.mobilemasrtask.auth.data.repository.AuthRepositoryImpl
import com.hossam.mobilemasrtask.auth.domain.repository.IAuthRepository
import com.hossam.mobilemasrtask.product.data.repository.ProductRepositoryImpl
import com.hossam.mobilemasrtask.product.domain.repository.IProductRepository
import com.hossam.mobilemasrtask.settings.data.repository.SettingRepositoryImpl
import com.hossam.mobilemasrtask.settings.domain.repository.ISettingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {


    @Binds
    @ViewModelScoped
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl): IAuthRepository

    @Binds
    @ViewModelScoped
    abstract fun bindProductRepository(productRepo: ProductRepositoryImpl): IProductRepository

    @Binds
    @ViewModelScoped
    abstract fun bindSettingRepository(settingRepository: SettingRepositoryImpl): ISettingRepository

}