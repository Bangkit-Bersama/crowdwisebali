package com.bangkit.crowdwisebali.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromAny(value: Any?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toAny(value: String?): Any? {
        return value
    }
}