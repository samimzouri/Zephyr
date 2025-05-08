package com.example.zephyr.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zephyr.model.WeatherResponse
import com.example.zephyr.ui.theme.ZephyrTheme
import com.airbnb.lottie.compose.*
import com.example.zephyr.R
import androidx.compose.ui.graphics.Color



@Composable
fun WeatherScreen(weather: WeatherResponse?) {
    ZephyrTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF001f3d)) { // Azul marino
            Box(modifier = Modifier.fillMaxSize()) {
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
                            .align(Alignment.TopCenter)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp)
                    ) {
                        // Ciudad y temperatura
                        Text(
                            text = "${it.name}, ${"%.1f".format(it.main.temp)}°C",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.White
                        )

                        Text(
                            text = description.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }

                } ?: run {
                    Text(
                        text = "No se ha podido obtener el clima",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White // Color de texto blanco
                    )
                }
            }
        }
    }
}


// Función para obtener el recurso Lottie según la descripción del clima
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

