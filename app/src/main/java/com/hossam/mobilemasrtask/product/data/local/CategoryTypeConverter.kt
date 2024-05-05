package com.hossam.mobilemasrtask.product.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hossam.mobilemasrtask.product.data.dto.CategoryDTO

class CategoryTypeConverter {

    private val gson = Gson()


    @TypeConverter
    fun categoryToString(category: CategoryDTO): String {
        return gson.toJson(category)
    }

    @TypeConverter
    fun stringToCategory(data: String): CategoryDTO {
        return gson.fromJson(data, CategoryDTO::class.java)
    }

}