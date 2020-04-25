package com.android.weatherapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.weatherapp.models.WeatherResp
import com.android.weatherapp.repositories.IWeatherRepo
import javax.inject.Inject

class WeatherActivityViewModel
@Inject constructor(
    private val repo: IWeatherRepo
): ViewModel() {
    val data: MutableLiveData<WeatherResp?> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val isFetching: MutableLiveData<Boolean> = MutableLiveData()
    private var savedCity: String? = null

    init {
        repo.loadCache()?.let { setData(it) }
    }

    fun loadData(city: String?){
        if(!isCityValid(city)){
            setError(CITY_VALIDATION_ERR)
            return
        }
        isFetching.value = true
        val data = repo.getWeather(city!!)
        setData(data)
    }

    fun currentCity() : String? = savedCity

    private fun setData(response: WeatherResp?) {
        if(response == null) {
            setError(repo.errorMsg ?: EMPTY_DATA_MSG)
            return
        }
        isFetching.value = false
        data.value = response
        savedCity = data.value?.city?.name
    }

    private fun setError(message: String) {
        isFetching.value = false
        errorMsg.value = message
    }

    companion object {
        const val EMPTY_DATA_MSG = "Server returned empty data"
        const val CITY_VALIDATION_ERR = "city must contain at least 3 letters"
        fun isCityValid(city: String?): Boolean =
            city != null && city.isNotEmpty() && city.length >= 3 && city.matches(Regex("[a-zA-Z-]+")) //city.matches (Regex("\\w+\\.?"))
    }
}