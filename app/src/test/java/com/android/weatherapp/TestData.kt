package com.android.weatherapp

import com.android.weatherapp.models.*

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
    1,3L, 4L
)

var cachedData: WeatherResp =  WeatherResp(
    1,
    "cached",
    1,
    1,
    listOf(weather),
    city
)
var remoteData: WeatherResp = WeatherResp(
    1,
    "remote",
    2,
    2,
    listOf(weather),
    city
)