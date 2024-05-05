package com.hossam.mobilemasrtask.di

import android.content.Context
import androidx.room.Room
import com.hossam.mobilemasrtask.common.data.local.room.MobileMasrDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MobileMasrDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = MobileMasrDatabase::class.java,
            name = MobileMasrDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: MobileMasrDatabase) = database.productDao()

}