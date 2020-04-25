package com.android.weatherapp.dagger

import android.content.Context
import com.android.weatherapp.views.WeatherActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepoModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(weatherActivity: WeatherActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun bindContext(context: Context) : Builder
        fun build(): AppComponent
    }
}