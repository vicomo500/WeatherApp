package com.android.weatherapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.android.weatherapp.models.WeatherResp

@Dao
interface WeatherDao {
    @Insert(onConflict = REPLACE)
    fun save(weather: WeatherResp)

    @Query("SELECT * FROM weather_data LIMIT 1")
    fun load(): WeatherResp?

    @Query("DELETE FROM weather_data")
    fun clear()
}