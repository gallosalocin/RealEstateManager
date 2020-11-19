package com.openclassrooms.realestatemanager.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromArrayListBoolean(list: ArrayList<Boolean>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrayListBoolean(value: String): ArrayList<Boolean> {
        val listType = object : TypeToken<ArrayList<Boolean>>(){}.type
        return Gson().fromJson(value, listType)
    }


}