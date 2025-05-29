package com.example.zephyr.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.zephyr.R
import com.example.zephyr.model.ForecastItem
import com.example.zephyr.model.ForecastResponse
import com.example.zephyr.model.WeatherResponse
import com.example.zephyr.ui.theme.ZephyrTheme

@Composable
fun WeatherScreen(weather: WeatherResponse?, forecast: ForecastResponse?) {
    ZephyrTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF001f3d)) {
            Column(modifier = Modifier.fillMaxSize()) {
                weather?.let {
                    val description = it.weather[0].description
                    Log.d("WeatherDebug", "Descripción real recibida: '$description'")

                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(getLottieRawRes(description))
                    )

                    LottieAnimation(
                        composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "${it.name}, ${"%.1f".format(it.main.temp)}°C",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.White
                        )
                    }
                } ?: run {
                    Text(
                        text = "No se ha podido obtener el clima",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }

                forecast?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        items(it.list) { forecastItem ->
                            ForecastItemView(forecastItem)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastItemView(forecastItem: ForecastItem) {
    val date = forecastItem.dt_txt.split(" ")[0]
    val dayOfWeek = getDayOfWeek(date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$dayOfWeek")
            Text(text = "Temperatura: ${"%.1f".format(forecastItem.main.temp)}°C")
            Text(text = "Descripción: ${forecastItem.weather[0].description}")
        }
    }
}

fun getDayOfWeek(date: String): String {
    val days = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
    val parts = date.split("-")
    val year = parts[0].toInt()
    val month = parts[1].toInt() - 1
    val day = parts[2].toInt()

    val calendar = java.util.Calendar.getInstance()
    calendar.set(year, month, day)
    val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1

    return days[dayOfWeek]
}

fun getLottieRawRes(description: String): Int {
    val desc = description.lowercase()
    return when {
        "clear" in desc || "despejado" in desc -> R.raw.sun
        "cloud" in desc || "nube" in desc || "nubes" in desc -> R.raw.cloud
        "rain" in desc || "lluvia" in desc || "lluvioso" in desc -> R.raw.rain
        "storm" in desc || "tormenta" in desc -> R.raw.storm
        "snow" in desc || "nieve" in desc -> R.raw.snow
        "mist" in desc || "fog" in desc || "niebla" in desc -> R.raw.fog
        else -> R.raw.sun
    }
}
