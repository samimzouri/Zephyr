package com.example.zephyr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zephyr.model.ForecastResponse
import com.example.zephyr.model.WeatherResponse
import com.example.zephyr.network.RetrofitClient
import com.example.zephyr.ui.screens.WeatherScreen
import com.example.zephyr.ui.theme.ZephyrTheme
import kotlinx.coroutines.launch
import com.example.zephyr.model.ForecastItem



class MainActivity : ComponentActivity() {
    private val apiKey = "1dff91ca3acb01f121fbfcd11fb90990" // Clave de la API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var city by remember { mutableStateOf("Málaga") }
            var weather by remember { mutableStateOf<WeatherResponse?>(null) }
            var forecast by remember { mutableStateOf<ForecastResponse?>(null) }
            val scope = rememberCoroutineScope()

            ZephyrTheme {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Zephyr ☁️",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("Ciudad") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            weather = RetrofitClient.api.getWeatherByCity(city, apiKey)
                                            forecast = RetrofitClient.api.getFiveDayForecast(city, apiKey)
                                            // Filtrar la previsión para mostrar solo una por día
                                            forecast?.let { forecastResponse ->
                                                forecast = filterForecast(forecastResponse)
                                            }
                                            navController.navigate("weather")
                                        } catch (e: Exception) {
                                            weather = null
                                            forecast = null
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Consultar clima")
                            }
                        }
                    }

                    composable("weather") {
                        WeatherScreen(weather = weather, forecast = forecast)
                    }
                }
            }
        }
    }

    private fun filterForecast(forecastResponse: ForecastResponse): ForecastResponse {
        val filteredList = mutableListOf<ForecastItem>()
        val seenDays = mutableSetOf<String>()

        for (item in forecastResponse.list) {
            val day = item.dt_txt.split(" ")[0] // Obtener solo la fecha (YYYY-MM-DD)
            if (!seenDays.contains(day)) {
                filteredList.add(item)
                seenDays.add(day)
                if (seenDays.size >= 5) break // Solo necesitamos 5 días
            }
        }

        return ForecastResponse(filteredList)
    }
}
