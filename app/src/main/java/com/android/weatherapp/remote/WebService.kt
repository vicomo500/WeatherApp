package com.android.weatherapp.remote

import com.android.weatherapp.models.WeatherResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET(".")
    fun getWeather(@Query("q") city: String): Call<WeatherResp>
}