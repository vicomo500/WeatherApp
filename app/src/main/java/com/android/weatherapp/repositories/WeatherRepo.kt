package com.android.weatherapp.repositories

import com.android.weatherapp.models.WeatherResp
import com.android.weatherapp.persistence.WeatherDao
import com.android.weatherapp.remote.WebService
import java.lang.Exception
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject

class WeatherRepo
    @Inject constructor(
    private val webService: WebService,
    private val dao: WeatherDao
) : IWeatherRepo {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    override var errorMsg: String? = null

    override fun getWeather(city: String): WeatherResp?{
        val futureTask: Future<WeatherResp?> = executor.submit(Callable {
            try{
                val response = webService.getWeather(city).execute()
                val body = response.body()
                body?.let {
                    cacheData(it)
                    errorMsg = null
                }
                return@Callable body
            }catch (ex: Exception){
                ex.printStackTrace()
                errorMsg = ex.message
                return@Callable null
            }
        })
        return try {
            futureTask.get()
        }catch (ex: InterruptedException){
            null
        }
    }

    override fun loadCache(): WeatherResp?{
        val futureTask: Future<WeatherResp?> = executor.submit(Callable {
            try{
                val data = dao.load()
                data?.let {
                    errorMsg = null
                }
                return@Callable data
            }catch (ex: Exception){
                ex.printStackTrace()
                errorMsg = ex.message
                return@Callable null
            }
        })
        return try {
            futureTask.get()
        }catch (ex: InterruptedException){
            null
        }
    }

    private fun cacheData(cache: WeatherResp){
        executor.execute {
            dao.clear()
            dao.save(cache)
        }
    }
}