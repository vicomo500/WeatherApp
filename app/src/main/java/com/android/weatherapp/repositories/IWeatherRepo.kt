package com.android.weatherapp.repositories

import com.android.weatherapp.models.WeatherResp

interface IWeatherRepo {
    var errorMsg: String?
    fun getWeather(city: String): WeatherResp?
    fun loadCache(): WeatherResp?
}