package com.example.zephyr.network

import com.example.zephyr.model.ForecastResponse
import com.example.zephyr.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): ForecastResponse
}
