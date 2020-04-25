package com.android.weatherapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.weatherapp.models.WeatherResp

@Database(entities = [WeatherResp::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    companion object {
        private const val DB_NAME = "weather_db"
        fun getInstance(context: Context) : WeatherDatabase =
            Room.databaseBuilder(context, WeatherDatabase::class.java, DB_NAME).build()
    }
}