package com.android.weatherapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.android.weatherapp.models.*
import com.android.weatherapp.persistence.WeatherDatabase
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase

    private lateinit var weatherResp: WeatherResp

    @Before
    fun setUp() {
        //database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        //test object
        val weather = Weather(
            11,
            Main(10F,2.1F,2.9F,2.0F,
                3,1,3,4,0.1F),
            listOf(),
            Cloud(12),
            Wind(10.4F, 43),
            Sys("sys"),
            "2020-04-23"
        )
        val city = City(
            1,
            "Lagos",
            Coordinate(2.2,2.2),
            "NG",
            200100,
            1,3L, 4L)
        weatherResp = WeatherResp(
            1,
            "code",
            21,
            1,
            listOf(weather),
            city
            )
    }

    @After
    fun cleanUp() = database.close()

    @Test
    fun saveData() {
        // GIVEN - weather is cached
        database.weatherDao().save(weatherResp)
        // WHEN - load cached weather
        val weather = database.weatherDao().load()
        // THEN - contains the expected values
        MatcherAssert.assertThat(weather != null, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(weather!!.id, CoreMatchers.`is`(weatherResp.id))
        MatcherAssert.assertThat(weather.code, CoreMatchers.`is`(weatherResp.code))
        MatcherAssert.assertThat(weather.city.name, CoreMatchers.`is`(weatherResp.city.name))
    }

    @Test
    fun clearDB(){
        // GIVEN - weather is cached
        database.weatherDao().save(weatherResp)
        // WHEN - clear cached data
        database.weatherDao().clear()
        // THEN - no data in DB
        val weather = database.weatherDao().load()
        MatcherAssert.assertThat(weather == null, CoreMatchers.`is`(true))
    }
}