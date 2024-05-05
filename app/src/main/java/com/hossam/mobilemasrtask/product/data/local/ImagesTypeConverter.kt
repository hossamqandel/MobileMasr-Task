package com.hossam.mobilemasrtask.product.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImagesTypeConverter {

    //Note -> Should be injected instead
    private val gson = Gson()

    @TypeConverter
    fun fromJsonToSet(json: String): Set<String> {
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJsonFromSet(data: Set<String>): String {
        return gson.toJson(data)
    }

}