package com.bangkit.crowdwisebali.helper

import androidx.room.TypeConverter
import com.bangkit.crowdwisebali.data.remote.response.PhotosItemDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromAny(value: Any?): String? {
        return value?.toString() // Konversi `Any` ke `String`
    }

    @TypeConverter
    fun toAny(value: String?): Any? {
        return value // Tetap sebagai String, atau ubah sesuai kebutuhan
    }
}