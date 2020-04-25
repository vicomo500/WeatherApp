package com.android.weatherapp

import com.android.weatherapp.models.*
import com.android.weatherapp.repositories.IWeatherRepo

class FakeWeatherRepo (
    val cachedData: WeatherResp?,
    val remoteData: WeatherResp?
): IWeatherRepo {

    override var errorMsg: String? = null

    override fun getWeather(city: String): WeatherResp? =
        if(city == "Lagos") {
            errorMsg = null
            remoteData
        } else {
            errorMsg = "cannot find weather for city"
            null
        }

    override fun loadCache(): WeatherResp? = cachedData

    companion object{
        private val weather = Weather (
            11,
            Main(10F,2.1F,2.9F,2.0F,
                3,1,3,4,0.1F),
            listOf(),
            Cloud(12),
            Wind(10.4F, 43),
            Sys("sys"),
            "2020-04-23"
        )
        private val city = City(
            1,
            "Lagos",
            Coordinate(2.2,2.2),
            "NG",
            200100,
            1,3L, 4L)
    }
}