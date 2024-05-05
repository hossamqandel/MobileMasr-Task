package com.hossam.mobilemasrtask.common.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hossam.mobilemasrtask.product.data.local.CategoryTypeConverter
import com.hossam.mobilemasrtask.product.data.local.ImagesTypeConverter
import com.hossam.mobilemasrtask.product.data.local.ProductDAO
import com.hossam.mobilemasrtask.product.data.local.ProductEntity

@Database(
    entities = [ProductEntity::class],
    exportSchema = false,
    version = 1,
)
@TypeConverters(
    CategoryTypeConverter::class,
    ImagesTypeConverter::class,
)
abstract class MobileMasrDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "mobile_masr_db"
    }


    abstract fun productDao(): ProductDAO

}