package com.android.amm.weatherapp.models

data class WeatherListUiState(
    val cityName: String? = null,
    val weatherList: List<DailyForecastWeather>? = null,
    val isWeatherListEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
