package com.hossam.mobilemasrtask.di

import com.hossam.mobilemasrtask.product.presentation.product_detail.ProductImageAdapter
import com.hossam.mobilemasrtask.product.presentation.products.ProductAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {


    @Provides
    @ActivityScoped
    fun provideProductAdapter() = ProductAdapter()


    @Provides
    @ActivityScoped
    fun provideProductImageAdapter() = ProductImageAdapter()

}