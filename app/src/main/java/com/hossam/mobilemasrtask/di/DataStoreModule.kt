package com.hossam.mobilemasrtask.di

import android.content.Context
import com.hossam.mobilemasrtask.common.data.local.datastore.DataStoreUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreUtil {
        return DataStoreUtil(context = context)
    }

}