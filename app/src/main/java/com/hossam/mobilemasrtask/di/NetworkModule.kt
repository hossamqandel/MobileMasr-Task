package com.hossam.mobilemasrtask.di

import com.hossam.mobilemasrtask.common.data.local.datastore.DataStoreUtil
import com.hossam.mobilemasrtask.common.data.remote.AuthInterceptor
import com.hossam.mobilemasrtask.common.data.remote.WebService
import com.hossam.mobilemasrtask.util.ConstAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideAuthInterceptor(dataStore: DataStoreUtil): AuthInterceptor {
        return AuthInterceptor(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConstAPI.BASE_URL_V1)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

     @Provides
     @Singleton
     fun provideWebService(retrofit: Retrofit): WebService {
         return retrofit.create(WebService::class.java)
     }

    
}