package com.example.zephyr.model

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String
)

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long, // Timestamp
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String // Fecha y hora en formato de texto
)
