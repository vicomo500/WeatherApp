package com.android.weatherapp.dagger

import android.content.Context
import com.android.weatherapp.remote.BASE_URL
import com.android.weatherapp.remote.WebService
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.android.weatherapp.BuildConfig
import com.android.weatherapp.persistence.WeatherDatabase
import com.android.weatherapp.repositories.IWeatherRepo
import com.android.weatherapp.repositories.WeatherRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepoModule {

    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideWebService(): WebService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(
                    OkHttpClient.Builder().addInterceptor { chain ->
                        val url = chain
                            .request()
                            .url()
                            .newBuilder()
                            .addQueryParameter("appid", BuildConfig.API_KEY)
                            .build()
                        chain.proceed(chain.request().newBuilder().url(url).build())
                    }.build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WebService::class.java)
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideWeatherDao(context: Context) = WeatherDatabase.getInstance(context).weatherDao()
    }

    @Binds
    abstract fun bindWeatherRepo(repoImpl: WeatherRepo): IWeatherRepo

}