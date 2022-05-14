package com.android.amm.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class WeatherListResponse(
    val daily: List<DailyForecastWeather>?
)

@Entity
data class DailyForecastWeather(
    @PrimaryKey var dt: String,
    var temp: Temp,
    var weather: List<Weather>
)

data class Temp(
    val day: Double,
    val night: Double,
)

data class Weather(
    val main: String,
    val icon: String
)

data class WeatherUiState(
    var date: String,
    var dayTemp: String,
    var nightTemp: String,
    var status: String,
    var icon: String
)
