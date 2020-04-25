package com.android.weatherapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.weatherapp.persistence.RoomConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_data")
@TypeConverters(RoomConverter::class)
data class WeatherResp (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @SerializedName("cod") @Expose val code: String,
    @Expose val message: Int,
    @SerializedName("cnt") @Expose val count: Int,
    var list: List<Weather>,
    var city: City
)

data class Weather (
    val dt: Long,
    val main: Main,
    val weather: List<WeatherItem>,
    val clouds: Cloud,
    val wind: Wind,
    val sys: Sys,
    @SerializedName("dt_txt") val date: String
)

data class Main (
    val temp: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("temp_min") val tempMin: Float,
    @SerializedName("temp_max") val tempMax: Float,
    val pressure: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val groundLevel: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("temp_kf") val tempKf: Float
)

data class WeatherItem (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Cloud (
    val all: Int
)

data class Wind (
    val speed: Float,
    val deg: Int
)

data class Sys (
    val pod: String
)

data class City (
    val id: Long,
    val name: String,
    @Expose (serialize = false, deserialize = true)
    @SerializedName("coord") val coordinate: Coordinate,
    val country: String,
    val population: Long,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Coordinate(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)