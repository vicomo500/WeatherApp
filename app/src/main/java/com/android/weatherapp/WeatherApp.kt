package com.android.weatherapp

import android.app.Application
import com.android.weatherapp.dagger.AppComponent
import com.android.weatherapp.dagger.DaggerAppComponent

class WeatherApp: Application() {
    private lateinit var daggerComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerAppComponent
                    .builder()
                    .bindContext(this)
                    .build()

    }

    fun daggerComponent(): AppComponent = daggerComponent
}