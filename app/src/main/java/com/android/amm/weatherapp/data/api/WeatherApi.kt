package com.android.amm.weatherapp.data.api

import com.android.amm.weatherapp.models.WeatherListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("onecall")
    suspend fun getWeatherList(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String): Response<WeatherListResponse>
}