package com.android.weatherapp.persistence

import android.text.TextUtils
import androidx.room.TypeConverter
import com.android.weatherapp.models.City
import com.android.weatherapp.models.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class RoomConverter {

    @TypeConverter
    fun serializeCity(value: City) : String = Gson().toJson(value)

    @TypeConverter
    fun deserializeCity(value: String): City?{
        try{
            if(TextUtils.isEmpty(value) || value == "null")
                return null
            return Gson().fromJson(value, City::class.java)
        }catch (ex: Exception){return null}
    }

    @TypeConverter
    fun serializeWeatherList(value: List<Weather>): String = Gson().toJson(value)

    @TypeConverter
    fun deserializeWeatherList(value: String): List<Weather>?{
        try{
            if( TextUtils.isEmpty(value)) return listOf()
            val gson = Gson()
            val type = object : TypeToken<List<Weather>?>() {}.type
            return gson.fromJson(value, type)
        }catch (ex: Exception){ return listOf() }
    }
}